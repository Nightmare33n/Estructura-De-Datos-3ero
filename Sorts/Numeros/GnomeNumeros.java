package Sorts.Numeros;
import java.util.Arrays;

// Gnome sort: un gnomo acomodando macetas. Si la de enfrente esta bien avanza,
// si no, las cambia y da un pasito pa' atras. Simple pero lento
public class GnomeNumeros {

    static void gnome(int[] a) {
        int i = 0;
        while (i < a.length) {
            if (i == 0 || a[i - 1] <= a[i]) {
                i++; // todo bien, avanzamos
            } else {
                int tmp = a[i];
                a[i] = a[i - 1];
                a[i - 1] = tmp;
                i--; // regresamos a checar
            }
        }
    }

    public static void main(String[] args) {
        int[] a = {29, 3, 72, 1, 45, 8, 16};
        System.out.println(Arrays.toString(a));
        gnome(a);
        System.out.println(Arrays.toString(a));
    }
}
