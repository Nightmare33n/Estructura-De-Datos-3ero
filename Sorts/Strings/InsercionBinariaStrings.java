package Sorts.Strings;
import java.util.Arrays;

// Insercion binaria con Strings: aqui si se nota la ventaja, porque comparar Strings
// cuesta mas que comparar numeros y la binaria hace menos comparaciones
public class InsercionBinariaStrings {

    static void insercionBinaria(String[] a) {
        for (int i = 1; i < a.length; i++) {
            String actual = a[i];
            int lo = 0, hi = i;
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                if (a[mid].compareTo(actual) <= 0) lo = mid + 1;
                else hi = mid;
            }
            for (int j = i; j > lo; j--) a[j] = a[j - 1];
            a[lo] = actual;
        }
    }

    public static void main(String[] args) {
        String[] a = {"pera", "manzana", "uva", "kiwi", "banana", "mango", "fresa"};
        System.out.println(Arrays.toString(a));
        insercionBinaria(a);
        System.out.println(Arrays.toString(a));
    }
}
