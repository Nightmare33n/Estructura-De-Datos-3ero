package Sorts.Strings;
import java.util.Arrays;

// Burbuja con banderita para Strings: si no hubo cambios en una vuelta, nos salimos
public class BurbujaMejoradaStrings {

    static void burbujaMejorada(String[] a) {
        boolean huboCambio = true;
        for (int i = 0; i < a.length - 1 && huboCambio; i++) {
            huboCambio = false;
            for (int j = 0; j < a.length - 1 - i; j++) {
                if (a[j].compareTo(a[j + 1]) > 0) {
                    String tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                    huboCambio = true;
                }
            }
        }
    }

    public static void main(String[] args) {
        String[] a = {"pera", "manzana", "uva", "kiwi", "banana", "mango", "fresa"};
        System.out.println(Arrays.toString(a));
        burbujaMejorada(a);
        System.out.println(Arrays.toString(a));
    }
}
