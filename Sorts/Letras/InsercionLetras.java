package Sorts.Letras;
import java.util.Arrays;

// Insercion con letras: agarras una letra y la metes en su lugar entre las ya ordenadas
public class InsercionLetras {

    static void insercion(char[] a) {
        for (int i = 1; i < a.length; i++) {
            char actual = a[i];
            int j = i - 1;
            while (j >= 0 && a[j] > actual) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = actual;
        }
    }

    public static void main(String[] args) {
        char[] a = {'m', 'z', 'c', 'a', 'q', 'f', 'b'};
        System.out.println(Arrays.toString(a));
        insercion(a);
        System.out.println(Arrays.toString(a));
    }
}
