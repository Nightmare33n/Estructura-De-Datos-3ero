package Sorts.Letras;
import java.util.Arrays;

// Counting sort con letras: aqui brilla, porque el abecedario es un rango chiquito.
// Cuentas cuantas 'a', cuantas 'b'... y las reescribes en orden
public class ConteoLetras {

    static void conteo(char[] a) {
        if (a.length == 0) return;
        char min = a[0], max = a[0];
        for (char c : a) {
            if (c < min) min = c;
            if (c > max) max = c;
        }
        int[] cuenta = new int[max - min + 1];
        for (char c : a) cuenta[c - min]++;

        int k = 0;
        for (int v = 0; v < cuenta.length; v++) {
            while (cuenta[v]-- > 0) a[k++] = (char) (v + min);
        }
    }

    public static void main(String[] args) {
        char[] a = {'m', 'z', 'c', 'a', 'q', 'f', 'b'};
        System.out.println(Arrays.toString(a));
        conteo(a);
        System.out.println(Arrays.toString(a));
    }
}
