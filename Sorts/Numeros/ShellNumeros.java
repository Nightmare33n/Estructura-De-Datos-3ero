package Sorts.Numeros;
import java.util.Arrays;

// Shell: insercion pero comparando elementos lejanos primero (con un "salto"),
// y el salto se va achicando hasta 1. Asi los que estan muy lejos llegan rapido
public class ShellNumeros {

    static void shell(int[] a) {
        for (int salto = a.length / 2; salto > 0; salto /= 2) {
            // basicamente una insercion, pero brincando de "salto" en "salto"
            for (int i = salto; i < a.length; i++) {
                int actual = a[i];
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
        int[] a = {29, 3, 72, 1, 45, 8, 16};
        System.out.println(Arrays.toString(a));
        shell(a);
        System.out.println(Arrays.toString(a));
    }
}
