package BubbleInsertionSort;
/*
 * Programa que genera un array con números aleatorios
 * y lo ordena de menor a mayor usando el método Burbuja (Bubble Sort)
 * ejecutado en la GPU con OpenCL (librería JOCL).
 *
 * En la GPU miles de hilos trabajan al mismo tiempo, por eso se usa la
 * versión paralela de Burbuja: "Odd-Even Transposition Sort".
 * En cada fase se comparan TODOS los pares vecinos a la vez:
 *   fase par:   (0,1) (2,3) (4,5) ...
 *   fase impar: (1,2) (3,4) (5,6) ...
 * Después de n fases el array queda ordenado.
 *
 * Ejecutar desde la carpeta raíz del repositorio:
 *   java -cp lib/jocl-2.0.5.jar BubbleInsertionSort/BubbleSort.java
 */

import static org.jocl.CL.*;

import java.util.Random;
import java.util.Scanner;

import org.jocl.CL;
import org.jocl.CLException;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;

public class BubbleSort {

    // Código que corre en la GPU (lenguaje OpenCL C).
    // Cada hilo de la GPU compara un par de vecinos y los intercambia si están en desorden.
    private static final String KERNEL =
        "__kernel void compararIntercambiar(__global int* datos, const int fase, const int n) {\n" +
        "    int i = get_global_id(0) * 2 + fase;\n" +
        "    if (i + 1 < n && datos[i] > datos[i + 1]) {\n" +
        "        int temp = datos[i];\n" +
        "        datos[i] = datos[i + 1];\n" +
        "        datos[i + 1] = temp;\n" +
        "    }\n" +
        "}\n";

    private static cl_context contexto;
    private static cl_command_queue cola;
    private static cl_program programa;
    private static cl_kernel kernel;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        // 1. Pedir el tamaño del array
        System.out.print("Datos:");
        // Acepta separadores de miles: 1000000, 1,000,000 o 1.000.000
        int n = Integer.parseInt(sc.next().replaceAll("[,._]", ""));

        // 2. Llenar el array con números aleatorios entre 1 y 100
        int[] numeros = new int[n];
        for (int i = 0; i < n; i++) {
            numeros[i] = random.nextInt(1000000) + 1;
        }

        mostrarArray("Array original: ", numeros);

        // 3. Preparar la GPU (no cuenta dentro del tiempo de ordenamiento)
        inicializarGPU();

        // 4. Ordenar en la GPU midiendo el tiempo
        long inicio = System.nanoTime();
        bubbleSortGPU(numeros);
        long fin = System.nanoTime();

        mostrarArray("Array ordenado: ", numeros);
        System.out.printf("Tiempo de ordenamiento (GPU): %.6f segundos%n", (fin - inicio) / 1_000_000_000.0);

        liberarGPU();
        sc.close();
    }

    // Busca la GPU, crea el contexto y compila el kernel
    public static void inicializarGPU() {
        CL.setExceptionsEnabled(true);

        int[] numPlataformas = new int[1];
        clGetPlatformIDs(0, null, numPlataformas);
        cl_platform_id[] plataformas = new cl_platform_id[numPlataformas[0]];
        clGetPlatformIDs(plataformas.length, plataformas, null);

        // Tomar la primera GPU que se encuentre
        cl_device_id gpu = null;
        for (cl_platform_id plataforma : plataformas) {
            int[] numDispositivos = new int[1];
            try {
                clGetDeviceIDs(plataforma, CL_DEVICE_TYPE_GPU, 0, null, numDispositivos);
            } catch (CLException e) {
                continue; // esta plataforma no tiene GPU
            }
            cl_device_id[] dispositivos = new cl_device_id[numDispositivos[0]];
            clGetDeviceIDs(plataforma, CL_DEVICE_TYPE_GPU, dispositivos.length, dispositivos, null);
            gpu = dispositivos[0];
            break;
        }
        if (gpu == null) {
            throw new IllegalStateException("No se encontró ninguna GPU compatible con OpenCL");
        }
        System.out.println("" + nombreDispositivo(gpu));

        contexto = clCreateContext(null, 1, new cl_device_id[]{gpu}, null, null, null);
        cola = clCreateCommandQueueWithProperties(contexto, gpu, null, null);
        programa = clCreateProgramWithSource(contexto, 1, new String[]{KERNEL}, null, null);
        clBuildProgram(programa, 0, null, null, null, null);
        kernel = clCreateKernel(programa, "compararIntercambiar", null);
    }

    // Bubble Sort paralelo (Odd-Even Transposition) ejecutado en la GPU
    public static void bubbleSortGPU(int[] array) {
        int n = array.length;
        if (n < 2) {
            return; // con 0 o 1 elementos ya está ordenado
        }

        // Copiar el array a la memoria de la GPU
        long bytes = (long) Sizeof.cl_int * n;
        cl_mem memoria = clCreateBuffer(contexto, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR,
                bytes, Pointer.to(array), null);

        clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memoria));
        clSetKernelArg(kernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
        long[] numPares = {n / 2};

        // n fases, alternando pares e impares
        int ultimoPorcentaje = -1;
        for (int fase = 0; fase < n; fase++) {
            clSetKernelArg(kernel, 1, Sizeof.cl_int, Pointer.to(new int[]{fase % 2}));
            clEnqueueNDRangeKernel(cola, kernel, 1, null, numPares, null, 0, null, null);
            
            // Mostrar el avance cada 1% (clFinish espera a que la GPU  vaya al corriente)
            int porcentaje = (int) (100L * (fase + 1) / n);
            if (porcentaje != ultimoPorcentaje) {
                clFinish(cola);
                System.out.print("\rProgreso: " + porcentaje + "%");
                System.out.flush();
                ultimoPorcentaje = porcentaje;
            }
        }
        System.out.println();

        // Traer el resultado de vuelta (CL_TRUE espera a que la GPU termine)
        clEnqueueReadBuffer(cola, memoria, CL_TRUE, 0, bytes, Pointer.to(array), 0, null, null);
        clReleaseMemObject(memoria);
    }

    public static void liberarGPU() {
        clReleaseKernel(kernel);
        clReleaseProgram(programa);
        clReleaseCommandQueue(cola);
        clReleaseContext(contexto);
    }

    private static String nombreDispositivo(cl_device_id dispositivo) {
        long[] tamano = new long[1];
        clGetDeviceInfo(dispositivo, CL_DEVICE_NAME, 0, null, tamano);
        byte[] buffer = new byte[(int) tamano[0]];
        clGetDeviceInfo(dispositivo, CL_DEVICE_NAME, buffer.length, Pointer.to(buffer), null);
        return new String(buffer, 0, buffer.length - 1);
    }

    // Método auxiliar para mostrar el array (si es muy grande no se imprime)
    private static void mostrarArray(String titulo, int[] array) {
        if (array.length > 50) {
            System.out.println(titulo + "(" + array.length + " elementos, demasiados para mostrar)");
            return;
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) sb.append(", ");
        }
        sb.append("]");
        System.out.println(titulo + sb);
    }
}
