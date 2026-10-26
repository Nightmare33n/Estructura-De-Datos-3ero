package Sorts.Strings;
import java.util.Arrays;

// Comb sort con Strings: burbuja con hueco que se va encogiendo
public class PeineStrings {

    static void peine(String[] a) {
        int hueco = a.length;
        boolean ordenado = false;
        while (!ordenado) {
            hueco = (int) (hueco / 1.3);
            if (hueco <= 1) {
                hueco = 1;
                ordenado = true;
            }
            for (int i = 0; i + hueco < a.length; i++) {
                if (a[i].compareTo(a[i + hueco]) > 0) {
                    String tmp = a[i];
                    a[i] = a[i + hueco];
                    a[i + hueco] = tmp;
                    ordenado = false;
                }
            }
        }
    }

    public static void main(String[] args) {
        String[] a = {"pera", "manzana", "uva", "kiwi", "banana", "mango", "fresa"};
        System.out.println(Arrays.toString(a));
        peine(a);
        System.out.println(Arrays.toString(a));
    }
}
