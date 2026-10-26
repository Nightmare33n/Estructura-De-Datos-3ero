package Sorts.Letras;
import java.util.Arrays;

// Insercion binaria con letras: el hueco donde va la letra se busca con binaria
public class InsercionBinariaLetras {

    static void insercionBinaria(char[] a) {
        for (int i = 1; i < a.length; i++) {
            char actual = a[i];
            int lo = 0, hi = i;
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                if (a[mid] <= actual) lo = mid + 1;
                else hi = mid;
            }
            for (int j = i; j > lo; j--) a[j] = a[j - 1];
            a[lo] = actual;
        }
    }

    public static void main(String[] args) {
        char[] a = {'m', 'z', 'c', 'a', 'q', 'f', 'b'};
        System.out.println(Arrays.toString(a));
        insercionBinaria(a);
        System.out.println(Arrays.toString(a));
    }
}
