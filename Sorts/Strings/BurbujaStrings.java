package Sorts.Strings;
import java.util.Arrays;

// Burbuja con Strings: aqui ya no sirve el >, se usa compareTo.
// a.compareTo(b) > 0 significa que "a" va despues que "b" en el diccionario
// (si quieres ignorar mayusculas usa compareToIgnoreCase)
public class BurbujaStrings {

    static void burbuja(String[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - 1 - i; j++) {
                if (a[j].compareTo(a[j + 1]) > 0) {
                    String tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                }
            }
        }
    }

    public static void main(String[] args) {
        String[] a = {"pera", "manzana", "uva", "kiwi", "banana", "mango", "fresa"};
        System.out.println(Arrays.toString(a));
        burbuja(a);
        System.out.println(Arrays.toString(a));
    }
}
