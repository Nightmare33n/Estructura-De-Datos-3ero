package Sorts.Numeros;
import java.util.Arrays;

// Burbuja pero con banderita: si en una vuelta no cambiaste nada, ya esta ordenado y te sales
public class BurbujaMejoradaNumeros {

    static void burbujaMejorada(int[] a) {
        boolean huboCambio = true;
        for (int i = 0; i < a.length - 1 && huboCambio; i++) {
            huboCambio = false; // asumimos que ya quedo, si cambia algo lo volvemos true
            for (int j = 0; j < a.length - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    int tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                    huboCambio = true;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] a = {29, 3, 72, 1, 45, 8, 16};
        System.out.println(Arrays.toString(a));
        burbujaMejorada(a);
        System.out.println(Arrays.toString(a));
    }
}
