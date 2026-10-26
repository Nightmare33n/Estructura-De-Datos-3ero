package Sorts.Strings;
import java.util.Arrays;

// Cocktail con Strings: burbuja de ida y vuelta usando compareTo
public class CocktailStrings {

    static void cocktail(String[] a) {
        int ini = 0, fin = a.length - 1;
        boolean huboCambio = true;
        while (huboCambio) {
            huboCambio = false;
            for (int i = ini; i < fin; i++) {
                if (a[i].compareTo(a[i + 1]) > 0) {
                    swap(a, i, i + 1);
                    huboCambio = true;
                }
            }
            fin--;
            if (!huboCambio) break;
            huboCambio = false;
            for (int i = fin; i > ini; i--) {
                if (a[i - 1].compareTo(a[i]) > 0) {
                    swap(a, i - 1, i);
                    huboCambio = true;
                }
            }
            ini++;
        }
    }

    static void swap(String[] a, int i, int j) {
        String tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static void main(String[] args) {
        String[] a = {"pera", "manzana", "uva", "kiwi", "banana", "mango", "fresa"};
        System.out.println(Arrays.toString(a));
        cocktail(a);
        System.out.println(Arrays.toString(a));
    }
}
