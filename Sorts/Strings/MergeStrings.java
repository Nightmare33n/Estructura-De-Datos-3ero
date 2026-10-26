package Sorts.Strings;
import java.util.Arrays;

// Merge sort con Strings: divide, ordena cada mitad y junta como cremallera
public class MergeStrings {

    static void merge(String[] a, int ini, int fin) {
        if (fin - ini < 2) return;
        int mid = (ini + fin) / 2;
        merge(a, ini, mid);
        merge(a, mid, fin);
        mezclar(a, ini, mid, fin);
    }

    static void mezclar(String[] a, int ini, int mid, int fin) {
        String[] tmp = new String[fin - ini];
        int i = ini, j = mid, k = 0;
        while (i < mid && j < fin) tmp[k++] = (a[i].compareTo(a[j]) <= 0) ? a[i++] : a[j++];
        while (i < mid) tmp[k++] = a[i++];
        while (j < fin) tmp[k++] = a[j++];
        System.arraycopy(tmp, 0, a, ini, tmp.length);
    }

    public static void main(String[] args) {
        String[] a = {"pera", "manzana", "uva", "kiwi", "banana", "mango", "fresa"};
        System.out.println(Arrays.toString(a));
        merge(a, 0, a.length);
        System.out.println(Arrays.toString(a));
    }
}
