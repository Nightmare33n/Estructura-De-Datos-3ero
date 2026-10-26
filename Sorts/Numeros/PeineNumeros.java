package Sorts.Numeros;
import java.util.Arrays;

// Comb sort (peine): burbuja pero comparando con un hueco grande que se va encogiendo (entre 1.3).
// Asi las "tortugas" (chicos al final) se mueven rapido
public class PeineNumeros {

    static void peine(int[] a) {
        int hueco = a.length;
        boolean ordenado = false;
        while (!ordenado) {
            hueco = (int) (hueco / 1.3);
            if (hueco <= 1) {
                hueco = 1;
                ordenado = true; // con hueco 1 es burbuja; si no cambia nada, ya quedo
            }
            for (int i = 0; i + hueco < a.length; i++) {
                if (a[i] > a[i + hueco]) {
                    int tmp = a[i];
                    a[i] = a[i + hueco];
                    a[i + hueco] = tmp;
                    ordenado = false;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] a = {29, 3, 72, 1, 45, 8, 16};
        System.out.println(Arrays.toString(a));
        peine(a);
        System.out.println(Arrays.toString(a));
    }
}
