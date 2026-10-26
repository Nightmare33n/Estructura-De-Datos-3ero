package Sorts.Letras;
import java.util.Arrays;

// Merge sort con letras: partes a la mitad, ordenas cada lado y los juntas
public class MergeLetras {

    static void merge(char[] a, int ini, int fin) {
        if (fin - ini < 2) return;
        int mid = (ini + fin) / 2;
        merge(a, ini, mid);
        merge(a, mid, fin);
        mezclar(a, ini, mid, fin);
    }

    static void mezclar(char[] a, int ini, int mid, int fin) {
        char[] tmp = new char[fin - ini];
        int i = ini, j = mid, k = 0;
        while (i < mid && j < fin) tmp[k++] = (a[i] <= a[j]) ? a[i++] : a[j++];
        while (i < mid) tmp[k++] = a[i++];
        while (j < fin) tmp[k++] = a[j++];
        System.arraycopy(tmp, 0, a, ini, tmp.length);
    }

    public static void main(String[] args) {
        char[] a = {'m', 'z', 'c', 'a', 'q', 'f', 'b'};
        System.out.println(Arrays.toString(a));
        merge(a, 0, a.length);
        System.out.println(Arrays.toString(a));
    }
}
