package Sorts.Numeros;
import java.util.Arrays;

// Merge sort: divide y venceras. Partes el arreglo a la mitad, ordenas cada mitad
// (recursivo) y luego las juntas como cremallera
public class MergeNumeros {

    // fin es exclusivo, o sea trabajamos en [ini, fin)
    static void merge(int[] a, int ini, int fin) {
        if (fin - ini < 2) return; // uno solito ya esta ordenado
        int mid = (ini + fin) / 2;
        merge(a, ini, mid);
        merge(a, mid, fin);
        mezclar(a, ini, mid, fin);
    }

    static void mezclar(int[] a, int ini, int mid, int fin) {
        int[] tmp = new int[fin - ini];
        int i = ini, j = mid, k = 0;
        // vas agarrando el mas chico de las dos mitades
        while (i < mid && j < fin) tmp[k++] = (a[i] <= a[j]) ? a[i++] : a[j++];
        // lo que sobre se pega tal cual
        while (i < mid) tmp[k++] = a[i++];
        while (j < fin) tmp[k++] = a[j++];
        System.arraycopy(tmp, 0, a, ini, tmp.length);
    }

    public static void main(String[] args) {
        int[] a = {29, 3, 72, 1, 45, 8, 16};
        System.out.println(Arrays.toString(a));
        merge(a, 0, a.length);
        System.out.println(Arrays.toString(a));
    }
}
