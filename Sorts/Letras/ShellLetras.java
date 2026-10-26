package Sorts.Letras;
import java.util.Arrays;

// Shell con letras: insercion con saltos que se van haciendo chiquitos
public class ShellLetras {

    static void shell(char[] a) {
        for (int salto = a.length / 2; salto > 0; salto /= 2) {
            for (int i = salto; i < a.length; i++) {
                char actual = a[i];
                int j = i;
                while (j >= salto && a[j - salto] > actual) {
                    a[j] = a[j - salto];
                    j -= salto;
                }
                a[j] = actual;
            }
        }
    }

    public static void main(String[] args) {
        char[] a = {'m', 'z', 'c', 'a', 'q', 'f', 'b'};
        System.out.println(Arrays.toString(a));
        shell(a);
        System.out.println(Arrays.toString(a));
    }
}
