package Sorts.Letras;
import java.util.Arrays;

// Heap sort con letras: monticulo con la letra mas grande arriba, la sacas al final y repites
public class HeapLetras {

    static void heap(char[] a) {
        int n = a.length;
        for (int i = n / 2 - 1; i >= 0; i--) hundir(a, n, i);
        for (int fin = n - 1; fin > 0; fin--) {
            swap(a, 0, fin);
            hundir(a, fin, 0);
        }
    }

    static void hundir(char[] a, int n, int i) {
        while (true) {
            int mayor = i, izq = 2 * i + 1, der = 2 * i + 2;
            if (izq < n && a[izq] > a[mayor]) mayor = izq;
            if (der < n && a[der] > a[mayor]) mayor = der;
            if (mayor == i) return;
            swap(a, i, mayor);
            i = mayor;
        }
    }

    static void swap(char[] a, int i, int j) {
        char tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static void main(String[] args) {
        char[] a = {'m', 'z', 'c', 'a', 'q', 'f', 'b'};
        System.out.println(Arrays.toString(a));
        heap(a);
        System.out.println(Arrays.toString(a));
    }
}
