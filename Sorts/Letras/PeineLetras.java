package Sorts.Letras;
import java.util.Arrays;

// Comb sort con letras: burbuja con hueco que se encoge /1.3 cada vuelta
public class PeineLetras {

    static void peine(char[] a) {
        int hueco = a.length;
        boolean ordenado = false;
        while (!ordenado) {
            hueco = (int) (hueco / 1.3);
            if (hueco <= 1) {
                hueco = 1;
                ordenado = true;
            }
            for (int i = 0; i + hueco < a.length; i++) {
                if (a[i] > a[i + hueco]) {
                    char tmp = a[i];
                    a[i] = a[i + hueco];
                    a[i + hueco] = tmp;
                    ordenado = false;
                }
            }
        }
    }

    public static void main(String[] args) {
        char[] a = {'m', 'z', 'c', 'a', 'q', 'f', 'b'};
        System.out.println(Arrays.toString(a));
        peine(a);
        System.out.println(Arrays.toString(a));
    }
}
