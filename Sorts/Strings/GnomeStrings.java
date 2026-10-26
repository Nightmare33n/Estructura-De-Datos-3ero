package Sorts.Strings;
import java.util.Arrays;

// Gnome con Strings: avanza si va bien, si no cambia y se regresa
public class GnomeStrings {

    static void gnome(String[] a) {
        int i = 0;
        while (i < a.length) {
            if (i == 0 || a[i - 1].compareTo(a[i]) <= 0) {
                i++;
            } else {
                String tmp = a[i];
                a[i] = a[i - 1];
                a[i - 1] = tmp;
                i--;
            }
        }
    }

    public static void main(String[] args) {
        String[] a = {"pera", "manzana", "uva", "kiwi", "banana", "mango", "fresa"};
        System.out.println(Arrays.toString(a));
        gnome(a);
        System.out.println(Arrays.toString(a));
    }
}
