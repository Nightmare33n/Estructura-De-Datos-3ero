package Sorts.Letras;
import java.util.Arrays;

// Radix con letras: como un char es un numero (su codigo, 'a' = 97), ordenamos por los
// digitos de ese codigo. Medio exagerado para letras sueltas, pero funciona igualito
public class RadixLetras {

    static void radix(char[] a) {
        if (a.length == 0) return;
        char max = a[0];
        for (char c : a) if (c > max) max = c;
        // los char nunca son negativos, asi que no hay que hacer truquitos
        for (int exp = 1; max / exp > 0; exp *= 10) porDigito(a, exp);
    }

    static void porDigito(char[] a, int exp) {
        char[] salida = new char[a.length];
        int[] cuenta = new int[10];
        for (char c : a) cuenta[(c / exp) % 10]++;
        for (int i = 1; i < 10; i++) cuenta[i] += cuenta[i - 1];
        for (int i = a.length - 1; i >= 0; i--) {
            salida[--cuenta[(a[i] / exp) % 10]] = a[i];
        }
        System.arraycopy(salida, 0, a, 0, a.length);
    }

    public static void main(String[] args) {
        char[] a = {'m', 'z', 'c', 'a', 'q', 'f', 'b'};
        System.out.println(Arrays.toString(a));
        radix(a);
        System.out.println(Arrays.toString(a));
    }
}
