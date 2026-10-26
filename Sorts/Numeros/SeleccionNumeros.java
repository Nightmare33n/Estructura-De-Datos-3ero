package Sorts.Numeros;
import java.util.Arrays;

// Seleccion: buscas el mas chiquito de lo que falta y lo pones al frente. Y asi hasta acabar
public class SeleccionNumeros {

    static void seleccion(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < a[min]) min = j;
            }
            // un solo cambio por vuelta, eso es lo bonito de este
            int tmp = a[i];
            a[i] = a[min];
            a[min] = tmp;
        }
    }

    public static void main(String[] args) {
        int[] a = {29, 3, 72, 1, 45, 8, 16};
        System.out.println(Arrays.toString(a));
        seleccion(a);
        System.out.println(Arrays.toString(a));
    }
}
