package Sorts.Numeros;
import java.util.Arrays;

// Insercion binaria: igual que insercion, pero el lugar donde va lo buscas con busqueda binaria
// (ahorras comparaciones, pero igual hay que recorrer los elementos)
public class InsercionBinariaNumeros {

    static void insercionBinaria(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int actual = a[i];
            int lo = 0, hi = i;
            // busqueda binaria en la parte que ya esta ordenada [0, i)
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                if (a[mid] <= actual) lo = mid + 1; // <= para que sea estable
                else hi = mid;
            }
            // hacemos espacio y la metemos
            for (int j = i; j > lo; j--) a[j] = a[j - 1];
            a[lo] = actual;
        }
    }

    public static void main(String[] args) {
        int[] a = {29, 3, 72, 1, 45, 8, 16};
        System.out.println(Arrays.toString(a));
        insercionBinaria(a);
        System.out.println(Arrays.toString(a));
    }
}
