package Sorts.Numeros;
import java.util.Arrays;

// Radix sort (LSD): ordena digito por digito, empezando por las unidades, luego decenas, etc.
// Cada pasada es un counting sort estable por ese digito
public class RadixNumeros {

    static void radix(int[] a) {
        if (a.length == 0) return;
        // truquito pa' los negativos: le restamos el minimo a todos y al final se lo regresamos
        int min = a[0];
        for (int x : a) if (x < min) min = x;
        for (int i = 0; i < a.length; i++) a[i] -= min;

        int max = a[0];
        for (int x : a) if (x > max) max = x;

        for (int exp = 1; max / exp > 0; exp *= 10) porDigito(a, exp);

        for (int i = 0; i < a.length; i++) a[i] += min;
    }

    static void porDigito(int[] a, int exp) {
        int[] salida = new int[a.length];
        int[] cuenta = new int[10];
        for (int x : a) cuenta[(x / exp) % 10]++;
        // sumas acumuladas = posicion final de cada digito
        for (int i = 1; i < 10; i++) cuenta[i] += cuenta[i - 1];
        // de atras pa' delante para que sea estable (importantisimo aqui)
        for (int i = a.length - 1; i >= 0; i--) {
            salida[--cuenta[(a[i] / exp) % 10]] = a[i];
        }
        System.arraycopy(salida, 0, a, 0, a.length);
    }

    public static void main(String[] args) {
        int[] a = {29, 3, 72, 1, 45, 8, 16};
        System.out.println(Arrays.toString(a));
        radix(a);
        System.out.println(Arrays.toString(a));
    }
}
