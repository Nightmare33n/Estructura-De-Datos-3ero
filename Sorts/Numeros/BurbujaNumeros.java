package Sorts.Numeros;
import java.util.Arrays;

// Burbuja clasico: comparas de a dos y el mas grande se va "flotando" hasta el final
public class BurbujaNumeros {

    static void burbuja(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            // cada vuelta deja al mas grande al final, por eso el -i (ya no hace falta revisarlo)
            for (int j = 0; j < a.length - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    int tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] a = {29, 3, 72, 1, 45, 8, 16};
        System.out.println(Arrays.toString(a));
        burbuja(a);
        System.out.println(Arrays.toString(a));
    }
}
