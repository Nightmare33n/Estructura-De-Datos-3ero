package Sorts.Numeros;
import java.util.Arrays;

// Cocktail (o sacudida): es burbuja pero de ida y vuelta, como agitando una coctelera
public class CocktailNumeros {

    static void cocktail(int[] a) {
        int ini = 0, fin = a.length - 1;
        boolean huboCambio = true;
        while (huboCambio) {
            huboCambio = false;
            // de ida: el grande se va pa' la derecha
            for (int i = ini; i < fin; i++) {
                if (a[i] > a[i + 1]) {
                    swap(a, i, i + 1);
                    huboCambio = true;
                }
            }
            fin--;
            if (!huboCambio) break;
            huboCambio = false;
            // de vuelta: el chico se va pa' la izquierda
            for (int i = fin; i > ini; i--) {
                if (a[i - 1] > a[i]) {
                    swap(a, i - 1, i);
                    huboCambio = true;
                }
            }
            ini++;
        }
    }

    static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static void main(String[] args) {
        int[] a = {29, 3, 72, 1, 45, 8, 16};
        System.out.println(Arrays.toString(a));
        cocktail(a);
        System.out.println(Arrays.toString(a));
    }
}
