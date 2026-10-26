package Sorts.Letras;
import java.util.Arrays;

// Burbuja con banderita pero con letras: si una vuelta no cambia nada, ya estuvo
public class BurbujaMejoradaLetras {

    static void burbujaMejorada(char[] a) {
        boolean huboCambio = true;
        for (int i = 0; i < a.length - 1 && huboCambio; i++) {
            huboCambio = false;
            for (int j = 0; j < a.length - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    char tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                    huboCambio = true;
                }
            }
        }
    }

    public static void main(String[] args) {
        char[] a = {'m', 'z', 'c', 'a', 'q', 'f', 'b'};
        System.out.println(Arrays.toString(a));
        burbujaMejorada(a);
        System.out.println(Arrays.toString(a));
    }
}
