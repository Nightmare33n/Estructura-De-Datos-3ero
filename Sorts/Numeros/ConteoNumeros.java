package Sorts.Numeros;
import java.util.Arrays;

// Counting sort: no compara nada, nomas cuenta cuantas veces sale cada numero y luego los reescribe.
// Jala chido si los numeros no estan muy dispersos (rango chico)
public class ConteoNumeros {

    static void conteo(int[] a) {
        if (a.length == 0) return;
        int min = a[0], max = a[0];
        for (int x : a) {
            if (x < min) min = x;
            if (x > max) max = x;
        }
        // usamos min como desfase, asi tambien aguanta negativos
        int[] cuenta = new int[max - min + 1];
        for (int x : a) cuenta[x - min]++;

        int k = 0;
        for (int v = 0; v < cuenta.length; v++) {
            while (cuenta[v]-- > 0) a[k++] = v + min;
        }
    }

    public static void main(String[] args) {
        int[] a = {29, 3, 72, 1, 45, 8, 16};
        System.out.println(Arrays.toString(a));
        conteo(a);
        System.out.println(Arrays.toString(a));
    }
}
