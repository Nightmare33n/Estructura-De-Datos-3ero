package Sorts.Letras;
import java.util.Arrays;

// Seleccion con letras: buscas la letra mas chica que falte y la mandas al frente
public class SeleccionLetras {

    static void seleccion(char[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < a[min]) min = j;
            }
            char tmp = a[i];
            a[i] = a[min];
            a[min] = tmp;
        }
    }

    public static void main(String[] args) {
        char[] a = {'m', 'z', 'c', 'a', 'q', 'f', 'b'};
        System.out.println(Arrays.toString(a));
        seleccion(a);
        System.out.println(Arrays.toString(a));
    }
}
