package Busqueda;

import java.util.Arrays;
import java.util.Random;
import javax.swing.JOptionPane;

public class MenuBusqueda {

    static int[] datos;          // datos originales (desordenados)
    static int[] datosOrdenados; // copia ordenada para binaria e interpolacion
    static int comparaciones;

    public static void main(String[] args) {
        generarDatos();
        if (datos == null) return;

        String[] opciones = {
            "1. Busqueda Secuencial (Lineal)",
            "2. Busqueda Binaria",
            "3. Busqueda por Interpolacion",
            "4. Generar nuevos datos",
            "5. Ver datos",
            "6. Salir"
        };

        while (true) {
            String opcion = (String) JOptionPane.showInputDialog(null,
                    "Datos generados: " + datos.length + "\nSelecciona una opcion:",
                    "Menu de Busqueda", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

            if (opcion == null || opcion.startsWith("6")) {
                JOptionPane.showMessageDialog(null, "Hasta luego!");
                break;
            }

            switch (opcion.charAt(0)) {
                case '1': buscar(1); break;
                case '2': buscar(2); break;
                case '3': buscar(3); break;
                case '4': generarDatos(); break;
                case '5': verDatos(); break;
            }
        }
    }

    static void generarDatos() {
        while (true) {
            String entrada = JOptionPane.showInputDialog(null,
                    "Cuantos datos aleatorios quieres generar?", "Generar datos",
                    JOptionPane.QUESTION_MESSAGE);
            if (entrada == null) return; // cancelar
            try {
                int n = Integer.parseInt(entrada.trim());
                if (n <= 0) {
                    JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a 0.");
                    continue;
                }
                Random r = new Random();
                datos = new int[n];
                for (int i = 0; i < n; i++) {
                    datos[i] = r.nextInt(n * 10) + 1; // rango 1 .. n*10
                }
                datosOrdenados = datos.clone();
                Arrays.sort(datosOrdenados);
                JOptionPane.showMessageDialog(null, "Se generaron " + n + " datos (rango 1 a " + (n * 10) + ").");
                return;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingresa un numero entero valido.");
            }
        }
    }

    static void verDatos() {
        int limite = Math.min(datos.length, 200);
        StringBuilder sb = new StringBuilder("Datos originales");
        if (datos.length > limite) sb.append(" (primeros ").append(limite).append(")");
        sb.append(":\n");
        for (int i = 0; i < limite; i++) {
            sb.append(datos[i]).append(i % 20 == 19 ? "\n" : ", ");
        }
        sb.append("\n\nDatos ordenados");
        if (datos.length > limite) sb.append(" (primeros ").append(limite).append(")");
        sb.append(":\n");
        for (int i = 0; i < limite; i++) {
            sb.append(datosOrdenados[i]).append(i % 20 == 19 ? "\n" : ", ");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Datos", JOptionPane.INFORMATION_MESSAGE);
    }

    static void buscar(int metodo) {
        String entrada = JOptionPane.showInputDialog(null, "Numero a buscar:");
        if (entrada == null) return;
        int valor;
        try {
            valor = Integer.parseInt(entrada.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingresa un numero entero valido.");
            return;
        }

        comparaciones = 0;
        long inicio = System.nanoTime();
        int pos;
        String nombre;
        String arreglo;
        switch (metodo) {
            case 1:
                pos = busquedaSecuencial(datos, valor);
                nombre = "Secuencial";
                arreglo = "arreglo original";
                break;
            case 2:
                pos = busquedaBinaria(datosOrdenados, valor);
                nombre = "Binaria";
                arreglo = "arreglo ordenado";
                break;
            default:
                pos = busquedaInterpolacion(datosOrdenados, valor);
                nombre = "Interpolacion";
                arreglo = "arreglo ordenado";
        }
        long tiempo = System.nanoTime() - inicio;

        String resultado = "Busqueda " + nombre + "\n\n"
                + (pos >= 0
                    ? "El numero " + valor + " SI se encontro en la posicion " + pos + " del " + arreglo + "."
                    : "El numero " + valor + " NO se encontro.")
                + "\nComparaciones: " + comparaciones
                + "\nTiempo: " + tiempo + " ns (" + String.format("%.4f", tiempo / 1_000_000.0) + " ms)";
        JOptionPane.showMessageDialog(null, resultado, "Resultado", JOptionPane.INFORMATION_MESSAGE);
    }

    // 1. Recorre el arreglo elemento por elemento. O(n)
    static int busquedaSecuencial(int[] a, int x) {
        for (int i = 0; i < a.length; i++) {
            comparaciones++;
            if (a[i] == x) return i;
        }
        return -1;
    }

    // 2. Divide el arreglo ordenado a la mitad en cada paso. O(log n)
    static int busquedaBinaria(int[] a, int x) {
        int izq = 0, der = a.length - 1;
        while (izq <= der) {
            int medio = izq + (der - izq) / 2;
            comparaciones++;
            if (a[medio] == x) return medio;
            if (a[medio] < x) izq = medio + 1;
            else der = medio - 1;
        }
        return -1;
    }

    // 3. Estima la posicion segun el valor buscado. O(log log n) promedio
    static int busquedaInterpolacion(int[] a, int x) {
        int bajo = 0, alto = a.length - 1;
        while (bajo <= alto && x >= a[bajo] && x <= a[alto]) {
            if (a[alto] == a[bajo]) {
                comparaciones++;
                return a[bajo] == x ? bajo : -1;
            }
            int pos = bajo + (int) ((long) (x - a[bajo]) * (alto - bajo) / (a[alto] - a[bajo]));
            comparaciones++;
            if (a[pos] == x) return pos;
            if (a[pos] < x) bajo = pos + 1;
            else alto = pos - 1;
        }
        return -1;
    }
}
