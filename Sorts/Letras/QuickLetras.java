package Sorts.Letras;
import java.util.Arrays;

// Quick sort con letras: pivote, chicos a la izquierda, grandes a la derecha, y repite
public class QuickLetras {

    static void quick(char[] a, int ini, int fin) {
        if (ini >= fin) return;
        int p = particion(a, ini, fin);
        quick(a, ini, p - 1);
        quick(a, p + 1, fin);
    }

    static int particion(char[] a, int ini, int fin) {
        char pivote = a[fin];
        int i = ini;
        for (int j = ini; j < fin; j++) {
            if (a[j] < pivote) {
                swap(a, i, j);
                i++;
            }
        }
        swap(a, i, fin);
        return i;
    }

    static void swap(char[] a, int i, int j) {
        char tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static void main(String[] args) {
        char[] a = {'m', 'z', 'c', 'a', 'q', 'f', 'b'};
        System.out.println(Arrays.toString(a));
        quick(a, 0, a.length - 1);
        System.out.println(Arrays.toString(a));
    }
}
