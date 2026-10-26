package Sorts.Letras;
import java.util.Arrays;

// Gnome con letras: avanzas si va bien, si no cambias y te regresas un paso
public class GnomeLetras {

    static void gnome(char[] a) {
        int i = 0;
        while (i < a.length) {
            if (i == 0 || a[i - 1] <= a[i]) {
                i++;
            } else {
                char tmp = a[i];
                a[i] = a[i - 1];
                a[i - 1] = tmp;
                i--;
            }
        }
    }

    public static void main(String[] args) {
        char[] a = {'m', 'z', 'c', 'a', 'q', 'f', 'b'};
        System.out.println(Arrays.toString(a));
        gnome(a);
        System.out.println(Arrays.toString(a));
    }
}
