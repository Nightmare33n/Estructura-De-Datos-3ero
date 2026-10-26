package Sorts.Numeros;
import java.util.Arrays;

// Insercion: como acomodar cartas en la mano, agarras una y la metes donde va entre las que ya ordenaste
public class InsercionNumeros {

    static void insercion(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int actual = a[i];
            int j = i - 1;
            // recorremos a la derecha todos los que son mas grandes que la carta
            while (j >= 0 && a[j] > actual) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = actual; // y aqui cae
        }
    }

    public static void main(String[] args) {
        int[] a = {29, 3, 72, 1, 45, 8, 16};
        System.out.println(Arrays.toString(a));
        insercion(a);
        System.out.println(Arrays.toString(a));
    }
}
