package Sorts.Strings;
import java.util.Arrays;

// Shell con Strings: insercion con saltos que se van achicando
public class ShellStrings {

    static void shell(String[] a) {
        for (int salto = a.length / 2; salto > 0; salto /= 2) {
            for (int i = salto; i < a.length; i++) {
                String actual = a[i];
                int j = i;
                while (j >= salto && a[j - salto].compareTo(actual) > 0) {
                    a[j] = a[j - salto];
                    j -= salto;
                }
                a[j] = actual;
            }
        }
    }

    public static void main(String[] args) {
        String[] a = {"pera", "manzana", "uva", "kiwi", "banana", "mango", "fresa"};
        System.out.println(Arrays.toString(a));
        shell(a);
        System.out.println(Arrays.toString(a));
    }
}
