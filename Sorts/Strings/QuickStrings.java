package Sorts.Strings;
import java.util.Arrays;

// Quick sort con Strings: pivote, las que van antes a la izquierda y las que van despues a la derecha
public class QuickStrings {

    static void quick(String[] a, int ini, int fin) {
        if (ini >= fin) return;
        int p = particion(a, ini, fin);
        quick(a, ini, p - 1);
        quick(a, p + 1, fin);
    }

    static int particion(String[] a, int ini, int fin) {
        String pivote = a[fin];
        int i = ini;
        for (int j = ini; j < fin; j++) {
            if (a[j].compareTo(pivote) < 0) {
                swap(a, i, j);
                i++;
            }
        }
        swap(a, i, fin);
        return i;
    }

    static void swap(String[] a, int i, int j) {
        String tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static void main(String[] args) {
        String[] a = {"pera", "manzana", "uva", "kiwi", "banana", "mango", "fresa"};
        System.out.println(Arrays.toString(a));
        quick(a, 0, a.length - 1);
        System.out.println(Arrays.toString(a));
    }
}
