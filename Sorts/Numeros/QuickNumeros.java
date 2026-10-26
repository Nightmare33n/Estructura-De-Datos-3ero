package Sorts.Numeros;
import java.util.Arrays;

// Quick sort: agarras un pivote, mandas los chicos a la izquierda y los grandes a la derecha,
// y repites con cada lado. En promedio es de los mas rapidos
public class QuickNumeros {

    // aqui ini y fin son inclusivos
    static void quick(int[] a, int ini, int fin) {
        if (ini >= fin) return;
        int p = particion(a, ini, fin);
        quick(a, ini, p - 1);
        quick(a, p + 1, fin);
    }

    // particion de Lomuto: el pivote es el ultimo
    static int particion(int[] a, int ini, int fin) {
        int pivote = a[fin];
        int i = ini; // aqui va el siguiente "chico"
        for (int j = ini; j < fin; j++) {
            if (a[j] < pivote) {
                swap(a, i, j);
                i++;
            }
        }
        swap(a, i, fin); // el pivote a su lugar final
        return i;
    }

    static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static void main(String[] args) {
        int[] a = {29, 3, 72, 1, 45, 8, 16};
        System.out.println(Arrays.toString(a));
        quick(a, 0, a.length - 1);
        System.out.println(Arrays.toString(a));
    }
}
