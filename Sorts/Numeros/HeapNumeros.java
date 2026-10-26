package Sorts.Numeros;
import java.util.Arrays;

// Heap sort: armas un monticulo (el mas grande siempre arriba), lo sacas y lo mandas al final,
// arreglas el monticulo y repites
public class HeapNumeros {

    static void heap(int[] a) {
        int n = a.length;
        // armar el monticulo desde los papas de abajo hacia arriba
        for (int i = n / 2 - 1; i >= 0; i--) hundir(a, n, i);
        // sacar el mayor (a[0]) y mandarlo al final
        for (int fin = n - 1; fin > 0; fin--) {
            swap(a, 0, fin);
            hundir(a, fin, 0);
        }
    }

    // baja el elemento i hasta donde le toca (hijos: 2i+1 y 2i+2)
    static void hundir(int[] a, int n, int i) {
        while (true) {
            int mayor = i, izq = 2 * i + 1, der = 2 * i + 2;
            if (izq < n && a[izq] > a[mayor]) mayor = izq;
            if (der < n && a[der] > a[mayor]) mayor = der;
            if (mayor == i) return; // ya esta donde va
            swap(a, i, mayor);
            i = mayor;
        }
    }

    static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static void main(String[] args) {
        int[] a = {29, 3, 72, 1, 45, 8, 16};
        System.out.println(Arrays.toString(a));
        heap(a);
        System.out.println(Arrays.toString(a));
    }
}
