package Sorts.Strings;
import java.util.Arrays;

// Seleccion con Strings: buscas la palabra que va primero en el diccionario y la pones al frente
public class SeleccionStrings {

    static void seleccion(String[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j].compareTo(a[min]) < 0) min = j;
            }
            String tmp = a[i];
            a[i] = a[min];
            a[min] = tmp;
        }
    }

    public static void main(String[] args) {
        String[] a = {"pera", "manzana", "uva", "kiwi", "banana", "mango", "fresa"};
        System.out.println(Arrays.toString(a));
        seleccion(a);
        System.out.println(Arrays.toString(a));
    }
}
