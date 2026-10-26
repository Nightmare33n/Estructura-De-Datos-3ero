package Sorts.Strings;
import java.util.Arrays;

// Heap sort con Strings: monticulo con la palabra "mas grande" (la ultima del diccionario) arriba
public class HeapStrings {

    static void heap(String[] a) {
        int n = a.length;
        for (int i = n / 2 - 1; i >= 0; i--) hundir(a, n, i);
        for (int fin = n - 1; fin > 0; fin--) {
            swap(a, 0, fin);
            hundir(a, fin, 0);
        }
    }

    static void hundir(String[] a, int n, int i) {
        while (true) {
            int mayor = i, izq = 2 * i + 1, der = 2 * i + 2;
            if (izq < n && a[izq].compareTo(a[mayor]) > 0) mayor = izq;
            if (der < n && a[der].compareTo(a[mayor]) > 0) mayor = der;
            if (mayor == i) return;
            swap(a, i, mayor);
            i = mayor;
        }
    }

    static void swap(String[] a, int i, int j) {
        String tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static void main(String[] args) {
        String[] a = {"pera", "manzana", "uva", "kiwi", "banana", "mango", "fresa"};
        System.out.println(Arrays.toString(a));
        heap(a);
        System.out.println(Arrays.toString(a));
    }
}
