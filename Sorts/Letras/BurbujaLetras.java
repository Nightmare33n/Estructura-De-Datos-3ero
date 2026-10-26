package Sorts.Letras;
import java.util.Arrays;

// Burbuja con letras: los char en Java son numeritos por dentro, asi que se comparan con > igualito
// ojo: 'Z' < 'a' porque las mayusculas van antes en la tabla ASCII
public class BurbujaLetras {

    static void burbuja(char[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    char tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                }
            }
        }
    }

    public static void main(String[] args) {
        char[] a = {'m', 'z', 'c', 'a', 'q', 'f', 'b'};
        System.out.println(Arrays.toString(a));
        burbuja(a);
        System.out.println(Arrays.toString(a));
    }
}
