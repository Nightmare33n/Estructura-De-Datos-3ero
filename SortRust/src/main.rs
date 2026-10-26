
use std::env;
use std::io::{self, Write};
use std::mem;
use std::process;
use std::ptr;
use std::thread;
use std::time::{Instant, SystemTime, UNIX_EPOCH};

use opencl3::command_queue::CommandQueue;
use opencl3::context::Context;
use opencl3::device::{CL_DEVICE_TYPE_GPU, Device, get_all_devices};
use opencl3::kernel::{ExecuteKernel, Kernel};
use opencl3::memory::{Buffer, CL_MEM_READ_WRITE};
use opencl3::program::Program;
use opencl3::types::{CL_BLOCKING, cl_uint};
use rayon::prelude::*;

/// Bits altos que deciden la cubeta (2^12 = 4096 cubetas).
const BITS_CUBETA: u32 = 12;
const NUM_CUBETAS: usize = 1 << BITS_CUBETA;
/// Los 20 bits bajos se ordenan con 2 pasadas de Radix Sort de 10 bits.
const BITS_RADIX: u32 = 10;
/// Cubetas más chicas que esto se ordenan con sort_unstable (el radix no conviene).
const MIN_RADIX: usize = 768;
/// En la GPU, los pasos del Bitonic Sort con distancia menor a esto se hacen juntos por bloque.
const BLOQUE_GPU: usize = 256;
/// Con menos datos la GPU no puede ganar (copiar a la GPU cuesta más que ordenar en el CPU).
const MIN_DATOS_GPU: usize = 1 << 20;

// Código que corre en la GPU (OpenCL C). Bitonic Sort: el arreglo debe medir una potencia de 2.
const KERNELS: &str = r#"
// Un paso completo: compara a[i] con a[i ^ j] en todo el arreglo.
kernel void bitonic_paso(global uint* a, const uint k, const uint j) {
    uint i = get_global_id(0);
    uint l = i ^ j;
    if (l > i) {
        uint x = a[i];
        uint y = a[l];
        bool ascendente = (i & k) == 0;
        if ((x > y) == ascendente) {
            a[i] = y;
            a[l] = x;
        }
    }
}

// Todos los pasos con distancia j <= j_inicio, dentro de cada bloque (un hilo de GPU por bloque).
// Así se hacen varios pasos en una sola llamada y la memoria que se toca está junta.
kernel void bitonic_bloque(global uint* a, const uint k, const uint j_inicio, const uint bloque) {
    uint base = get_global_id(0) * bloque;
    for (uint j = j_inicio; j > 0; j >>= 1) {
        for (uint i = base; i < base + bloque; i++) {
            uint l = i ^ j;
            if (l > i) {
                uint x = a[i];
                uint y = a[l];
                bool ascendente = (i & k) == 0;
                if ((x > y) == ascendente) {
                    a[i] = y;
                    a[l] = x;
                }
            }
        }
    }
}
"#;

fn main() {
    let (cantidad, porcentaje_gpu_pedido) = leer_argumentos();
    let n = cantidad.unwrap_or_else(pedir_cantidad);
    let hilos = rayon::current_num_threads();

    // 1. Datos aleatorios
    let original = generar_datos(n);
    let huella_original = huella(&original);

    // 2. Preparar GPU (no cuenta en el tiempo de ordenamiento). Si no hay GPU o falla, todo lo ordena el CPU.
    //    Sin porcentaje pedido se miden las velocidades para elegir el reparto más rápido.
    let usar_gpu = match porcentaje_gpu_pedido {
        Some(p) => p > 0.0,
        None => n >= MIN_DATOS_GPU,
    };
    let mut gpu = if usar_gpu { Gpu::nueva().ok() } else { None };
    let modelo = match porcentaje_gpu_pedido {
        Some(_) => None,
        None => gpu.as_ref().and_then(|g| medir_gpu(g).ok()).map(|(por_elemento, por_comparacion)| {
            Modelo {
                hilos,
                cpu_seg_por_elemento: medir_cpu(),
                gpu_seg_por_elemento: por_elemento,
                gpu_seg_por_comparacion: por_comparacion,
            }
        }),
    };

    // Memoria para ordenar (se toca antes para no medir el costo de pedirle memoria al sistema)
    let mut aux = original; // se usa como memoria auxiliar del radix (se sobrescribe)
    let mut resultado = vec![0u32; n];
    resultado.par_chunks_mut(1 << 16).for_each(|trozo| trozo.fill(u32::MAX));

    // 3. Ordenar
    let inicio = Instant::now();
    let inicios = repartir_en_cubetas(&aux, &mut resultado);

    let cubetas_gpu = match (&gpu, porcentaje_gpu_pedido, &modelo) {
        (Some(g), Some(p), _) => cubetas_para_porcentaje(&inicios, g.max_elementos, p),
        (Some(g), None, Some(m)) => mejor_reparto(&inicios, m, g.max_elementos),
        _ => 0,
    };
    let g = inicios[cubetas_gpu];

    let (parte_gpu, parte_cpu) = resultado.split_at_mut(g);
    let (_, aux_cpu) = aux.split_at_mut(g);
    let inicios_cpu: Vec<usize> = inicios[cubetas_gpu..].iter().map(|&i| i - g).collect();

    thread::scope(|s| {
        // Hilo que maneja la GPU
        let gpu_del_hilo = if g > 0 { gpu.take() } else { None };
        let hilo_gpu = gpu_del_hilo.map(|gpu| s.spawn(move || (gpu.ordenar(parte_gpu), parte_gpu)));

        // Mientras tanto, el CPU ordena sus cubetas con todos sus hilos
        ordenar_cubetas_cpu(parte_cpu, aux_cpu, &inicios_cpu);

        if let Some(h) = hilo_gpu {
            let (r, parte) = h.join().expect("el hilo de la GPU terminó con error");
            if r.is_err() {
                parte.par_sort_unstable(); // si la GPU falla, el CPU ordena esa parte
            }
        }
    });
    let segundos = inicio.elapsed().as_secs_f64();

    // 4. Resultado
    if n <= 20 {
        println!("{resultado:?}");
    }
    let porcentaje_gpu = if n > 0 { 100.0 * g as f64 / n as f64 } else { 0.0 };
    println!("GPU {porcentaje_gpu:.1}% | CPU {:.1}%", 100.0 - porcentaje_gpu);
    println!("Tiempo de ordenamiento: {segundos:.3} s");

    let correcto = resultado.par_windows(2).all(|w| w[0] <= w[1]) && huella(&resultado) == huella_original;
    if !correcto {
        println!("ERROR: el resultado no está bien ordenado");
    }
}

// ---------------------------------------------------------------- GPU

struct Gpu {
    contexto: Context,
    cola: CommandQueue,
    paso: Kernel,
    bloque: Kernel,
    /// Máximo de números que caben en un solo buffer de la GPU (potencia de 2).
    max_elementos: usize,
}

impl Gpu {
    fn nueva() -> Result<Gpu, String> {
        let id = *get_all_devices(CL_DEVICE_TYPE_GPU)
            .map_err(texto)?
            .first()
            .ok_or("no hay GPU con OpenCL")?;
        let dispositivo = Device::new(id);
        let contexto = Context::from_device(&dispositivo).map_err(texto)?;
        let cola = CommandQueue::create_default_with_properties(&contexto, 0, 0).map_err(texto)?;
        let programa = Program::create_and_build_from_source(&contexto, KERNELS, "")?;
        let paso = Kernel::create(&programa, "bitonic_paso").map_err(texto)?;
        let bloque = Kernel::create(&programa, "bitonic_bloque").map_err(texto)?;

        let max_bytes = dispositivo.max_mem_alloc_size().map_err(texto)? as usize;
        let max = (max_bytes / mem::size_of::<u32>()).min(1 << 30);
        let max_elementos = 1 << (usize::BITS - 1 - max.leading_zeros());

        Ok(Gpu {
            contexto,
            cola,
            paso,
            bloque,
            max_elementos,
        })
    }

    /// Ordena `datos` en la GPU con Bitonic Sort.
    /// Devuelve (segundos copiando datos, segundos ordenando).
    fn ordenar(&self, datos: &mut [u32]) -> Result<(f64, f64), String> {
        let len = datos.len();
        if len < 2 {
            return Ok((0.0, 0.0));
        }
        let n = len.next_power_of_two().max(BLOQUE_GPU);

        // Copiar a la GPU. Lo que falta para llegar a potencia de 2 se rellena con el
        // valor máximo: queda al final y no se regresa.
        let t = Instant::now();
        let mut buffer = unsafe {
            Buffer::<cl_uint>::create(&self.contexto, CL_MEM_READ_WRITE, n, ptr::null_mut())
        }
        .map_err(texto)?;
        unsafe { self.cola.enqueue_write_buffer(&mut buffer, CL_BLOCKING, 0, datos, &[]) }
            .map_err(texto)?;
        if n > len {
            let relleno = vec![u32::MAX; n - len];
            let desplazamiento = len * mem::size_of::<u32>();
            unsafe {
                self.cola
                    .enqueue_write_buffer(&mut buffer, CL_BLOCKING, desplazamiento, &relleno, &[])
            }
            .map_err(texto)?;
        }
        let mut t_copiar = t.elapsed().as_secs_f64();

        // Bitonic Sort
        let t = Instant::now();
        let bloques = n / BLOQUE_GPU;
        let mut k = 2;
        while k <= n {
            let mut j = k / 2;
            // Distancias grandes: un paso por llamada, sobre todo el arreglo
            while j >= BLOQUE_GPU {
                unsafe {
                    ExecuteKernel::new(&self.paso)
                        .set_arg(&buffer)
                        .set_arg(&(k as cl_uint))
                        .set_arg(&(j as cl_uint))
                        .set_global_work_size(n)
                        .enqueue_nd_range(&self.cola)
                }
                .map_err(texto)?;
                j /= 2;
            }
            // Distancias pequeñas: todos los pasos restantes en una llamada, por bloque
            unsafe {
                ExecuteKernel::new(&self.bloque)
                    .set_arg(&buffer)
                    .set_arg(&(k as cl_uint))
                    .set_arg(&(j as cl_uint))
                    .set_arg(&(BLOQUE_GPU as cl_uint))
                    .set_global_work_size(bloques)
                    .enqueue_nd_range(&self.cola)
            }
            .map_err(texto)?;
            k *= 2;
        }
        self.cola.finish().map_err(texto)?;
        let t_ordenar = t.elapsed().as_secs_f64();

        // Regresar el resultado
        let t = Instant::now();
        unsafe { self.cola.enqueue_read_buffer(&buffer, CL_BLOCKING, 0, datos, &[]) }
            .map_err(texto)?;
        t_copiar += t.elapsed().as_secs_f64();

        Ok((t_copiar, t_ordenar))
    }
}

/// Número de comparaciones de Bitonic Sort para n (potencia de 2): n · log n · (log n + 1) / 2.
fn comparaciones(n: usize) -> f64 {
    let log = n.trailing_zeros() as f64;
    n as f64 * log * (log + 1.0) / 2.0
}

// ---------------------------------------------------------------- Reparto GPU / CPU

/// Velocidades medidas para estimar cuánto tardaría cada reparto.
struct Modelo {
    hilos: usize,
    cpu_seg_por_elemento: f64,
    gpu_seg_por_elemento: f64,
    gpu_seg_por_comparacion: f64,
}

impl Modelo {
    /// Tiempo estimado si la GPU ordena `g` de los `n` números (el que tarde más de los dos).
    fn tiempo(&self, g: usize, n: usize) -> f64 {
        if g == 0 {
            return n as f64 * self.cpu_seg_por_elemento;
        }
        // Mientras la GPU trabaja, uno de los hilos del CPU la atiende
        let hilos = self.hilos as f64;
        let cpu = (n - g) as f64 * self.cpu_seg_por_elemento * hilos / (hilos - 1.0).max(1.0);
        let potencia = g.next_power_of_two().max(BLOQUE_GPU);
        let gpu = potencia as f64 * self.gpu_seg_por_elemento
            + comparaciones(potencia) * self.gpu_seg_por_comparacion;
        cpu.max(gpu)
    }
}

/// Cuántas cubetas (desde la primera) ordena la GPU para acercarse al porcentaje pedido.
fn cubetas_para_porcentaje(inicios: &[usize], max_gpu: usize, porcentaje: f64) -> usize {
    let objetivo = (inicios[NUM_CUBETAS] as f64 * porcentaje / 100.0).round() as usize;
    (0..=NUM_CUBETAS)
        .filter(|&c| inicios[c] <= max_gpu)
        .min_by_key(|&c| inicios[c].abs_diff(objetivo))
        .unwrap_or(0)
}

/// Cuántas cubetas (desde la primera) ordena la GPU para terminar lo antes posible.
fn mejor_reparto(inicios: &[usize], modelo: &Modelo, max_gpu: usize) -> usize {
    let n = inicios[NUM_CUBETAS];
    (0..=NUM_CUBETAS)
        .filter(|&c| inicios[c] <= max_gpu)
        .min_by(|&a, &b| modelo.tiempo(inicios[a], n).total_cmp(&modelo.tiempo(inicios[b], n)))
        .unwrap_or(0)
}

fn medir_gpu(gpu: &Gpu) -> Result<(f64, f64), String> {
    // La primera vez la GPU es más lenta (prepara el kernel), así que se calienta antes
    gpu.ordenar(&mut generar_datos(1 << 16))?;
    let m = MIN_DATOS_GPU.min(gpu.max_elementos);
    let (t_copiar, t_ordenar) = gpu.ordenar(&mut generar_datos(m))?;
    Ok((t_copiar / m as f64, t_ordenar / comparaciones(m)))
}

fn medir_cpu() -> f64 {
    let m = 1 << 23;
    let muestra = generar_datos(m);
    let mut resultado = vec![0u32; m];
    let mut aux = vec![0u32; m];
    let mut mejor = f64::MAX;
    for _ in 0..2 {
        let inicios = repartir_en_cubetas(&muestra, &mut resultado);
        let t = Instant::now();
        ordenar_cubetas_cpu(&mut resultado, &mut aux, &inicios);
        mejor = mejor.min(t.elapsed().as_secs_f64());
    }
    mejor / m as f64
}

// ---------------------------------------------------------------- CPU

fn cubeta(x: u32) -> usize {
    (x >> (32 - BITS_CUBETA)) as usize
}

/// Puntero que varios hilos pueden usar a la vez, siempre que escriban en posiciones distintas.
#[derive(Clone, Copy)]
struct PunteroCompartido(*mut u32);
unsafe impl Send for PunteroCompartido {}
unsafe impl Sync for PunteroCompartido {}

impl PunteroCompartido {
    fn get(self) -> *mut u32 {
        self.0
    }
}

/// Copia `origen` en `destino` agrupado por cubetas, en paralelo.
/// Devuelve dónde empieza cada cubeta (NUM_CUBETAS + 1 posiciones; la última es n).
fn repartir_en_cubetas(origen: &[u32], destino: &mut [u32]) -> Vec<usize> {
    assert_eq!(origen.len(), destino.len());
    let tam_trozo = origen.len().div_ceil(rayon::current_num_threads()).max(1);

    // Cada hilo cuenta cuántos números de su trozo caen en cada cubeta
    let conteos: Vec<Vec<usize>> = origen
        .par_chunks(tam_trozo)
        .map(|trozo| {
            let mut conteo = vec![0usize; NUM_CUBETAS];
            for &x in trozo {
                conteo[cubeta(x)] += 1;
            }
            conteo
        })
        .collect();

    // Dónde empieza cada cubeta en el arreglo final
    let mut inicios = vec![0usize; NUM_CUBETAS + 1];
    for b in 0..NUM_CUBETAS {
        inicios[b + 1] = inicios[b] + conteos.iter().map(|c| c[b]).sum::<usize>();
    }

    // Dónde escribe cada trozo dentro de cada cubeta (uno después del otro)
    let mut posicion = inicios[..NUM_CUBETAS].to_vec();
    let escrituras: Vec<Vec<usize>> = conteos
        .iter()
        .map(|conteo| {
            let mias = posicion.clone();
            for b in 0..NUM_CUBETAS {
                posicion[b] += conteo[b];
            }
            mias
        })
        .collect();

    // Cada hilo copia su trozo; ningún par de hilos escribe en la misma posición
    let destino_compartido = PunteroCompartido(destino.as_mut_ptr());
    origen
        .par_chunks(tam_trozo)
        .zip(escrituras)
        .for_each(|(trozo, mut posicion)| {
            let destino = destino_compartido.get();
            for &x in trozo {
                let b = cubeta(x);
                unsafe { *destino.add(posicion[b]) = x };
                posicion[b] += 1;
            }
        });

    inicios
}

/// Ordena cada cubeta por separado, muchas a la vez. El resultado queda en `datos`.
fn ordenar_cubetas_cpu(datos: &mut [u32], aux: &mut [u32], inicios: &[usize]) {
    let mut pares = Vec::with_capacity(inicios.len());
    let (mut resto_datos, mut resto_aux) = (datos, aux);
    for rango in inicios.windows(2) {
        let tam = rango[1] - rango[0];
        let (d, siguiente_d) = mem::take(&mut resto_datos).split_at_mut(tam);
        let (a, siguiente_a) = mem::take(&mut resto_aux).split_at_mut(tam);
        pares.push((d, a));
        resto_datos = siguiente_d;
        resto_aux = siguiente_a;
    }
    pares.into_par_iter().for_each(|(d, a)| ordenar_cubeta(d, a));
}

fn ordenar_cubeta(datos: &mut [u32], aux: &mut [u32]) {
    if datos.len() < MIN_RADIX {
        datos.sort_unstable();
        return;
    }
    // Radix Sort LSD: bits 0..10 (datos -> aux) y luego bits 10..20 (aux -> datos)
    pasada_radix(datos, aux, 0);
    pasada_radix(aux, datos, BITS_RADIX);
}

fn pasada_radix(origen: &[u32], destino: &mut [u32], desplazamiento: u32) {
    let mascara = (1u32 << BITS_RADIX) - 1;
    let mut posicion = [0usize; 1 << BITS_RADIX];
    for &x in origen {
        posicion[((x >> desplazamiento) & mascara) as usize] += 1;
    }
    let mut suma = 0;
    for p in posicion.iter_mut() {
        let cantidad = *p;
        *p = suma;
        suma += cantidad;
    }
    for &x in origen {
        let digito = ((x >> desplazamiento) & mascara) as usize;
        destino[posicion[digito]] = x;
        posicion[digito] += 1;
    }
}

// ---------------------------------------------------------------- Utilidades

fn generar_datos(n: usize) -> Vec<u32> {
    let semilla = SystemTime::now().duration_since(UNIX_EPOCH).map_or(0, |d| d.as_nanos() as u64);
    let mut datos = vec![0u32; n];
    datos.par_chunks_mut(1 << 16).enumerate().for_each(|(i, trozo)| {
        let mut estado = semilla ^ (i as u64).wrapping_mul(0x9E37_79B9_7F4A_7C15);
        for x in trozo {
            *x = (splitmix64(&mut estado) >> 32) as u32;
        }
    });
    datos
}

fn splitmix64(estado: &mut u64) -> u64 {
    *estado = estado.wrapping_add(0x9E37_79B9_7F4A_7C15);
    let mut z = *estado;
    z = (z ^ (z >> 30)).wrapping_mul(0xBF58_476D_1CE4_E5B9);
    z = (z ^ (z >> 27)).wrapping_mul(0x94D0_49BB_1331_11EB);
    z ^ (z >> 31)
}

/// Suma y XOR de todos los números: si cambian, se perdió o se duplicó algún número.
fn huella(datos: &[u32]) -> (u64, u32) {
    datos
        .par_iter()
        .fold(|| (0u64, 0u32), |(s, x), &v| (s.wrapping_add(v as u64), x ^ v))
        .reduce(|| (0, 0), |(s1, x1), (s2, x2)| (s1.wrapping_add(s2), x1 ^ x2))
}

/// Argumentos: [cantidad] [--gpu PORCENTAJE | --cpu PORCENTAJE]
/// Ejemplo: `100000000 --gpu 30` → la GPU ordena 30% de los datos y el CPU 70%.
/// Sin --gpu ni --cpu, el programa elige el reparto más rápido.
fn leer_argumentos() -> (Option<usize>, Option<f64>) {
    let mut cantidad = None;
    let mut gpu = None;
    let mut cpu = None;
    let mut args = env::args().skip(1);
    while let Some(arg) = args.next() {
        match arg.as_str() {
            "--gpu" => gpu = Some(leer_porcentaje(args.next())),
            "--cpu" => cpu = Some(leer_porcentaje(args.next())),
            _ => cantidad = Some(leer_cantidad(&arg)),
        }
    }
    let porcentaje_gpu = match (gpu, cpu) {
        (Some(g), Some(c)) if (g + c - 100.0).abs() > 1e-9 => salir("--gpu y --cpu deben sumar 100"),
        (Some(g), _) => Some(g),
        (None, Some(c)) => Some(100.0 - c),
        (None, None) => None,
    };
    (cantidad, porcentaje_gpu)
}

fn leer_porcentaje(texto: Option<String>) -> f64 {
    match texto.as_deref().map(|t| t.trim_end_matches('%').parse::<f64>()) {
        Some(Ok(p)) if (0.0..=100.0).contains(&p) => p,
        _ => salir("El porcentaje debe ser un número de 0 a 100"),
    }
}

fn salir(mensaje: &str) -> ! {
    eprintln!("{mensaje}");
    process::exit(1);
}

fn leer_cantidad(texto: &str) -> usize {
    let limpio: String = texto.trim().chars().filter(|c| !matches!(c, ',' | '.' | '_')).collect();
    limpio.parse().unwrap_or_else(|_| {
        eprintln!("Cantidad no válida: {texto}");
        process::exit(1);
    })
}

fn pedir_cantidad() -> usize {
    print!("Datos: ");
    io::stdout().flush().ok();
    let mut linea = String::new();
    io::stdin().read_line(&mut linea).ok();
    leer_cantidad(&linea)
}

fn texto<E: std::fmt::Display>(e: E) -> String {
    e.to_string()
}
