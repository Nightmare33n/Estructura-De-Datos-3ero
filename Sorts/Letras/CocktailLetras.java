package Sorts.Letras;
import java.util.Arrays;

// Cocktail con letras: burbuja de ida y vuelta
public class CocktailLetras {

    static void cocktail(char[] a) {
        int ini = 0, fin = a.length - 1;
        boolean huboCambio = true;
        while (huboCambio) {
            huboCambio = false;
            // ida: la letra mas "grande" se va al final
            for (int i = ini; i < fin; i++) {
                if (a[i] > a[i + 1]) {
                    swap(a, i, i + 1);
                    huboCambio = true;
                }
            }
            fin--;
            if (!huboCambio) break;
            huboCambio = false;
            // vuelta: la mas "chica" se va al inicio
            for (int i = fin; i > ini; i--) {
                if (a[i - 1] > a[i]) {
                    swap(a, i - 1, i);
                    huboCambio = true;
                }
            }
            ini++;
        }
    }

    static void swap(char[] a, int i, int j) {
        char tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static void main(String[] args) {
        char[] a = {'m', 'z', 'c', 'a', 'q', 'f', 'b'};
        System.out.println(Arrays.toString(a));
        cocktail(a);
        System.out.println(Arrays.toString(a));
    }
}
