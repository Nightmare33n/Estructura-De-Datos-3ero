package Sorts.Strings;
import java.util.Arrays;

// Insercion con Strings: metes cada palabra en su lugar entre las que ya estan ordenadas
public class InsercionStrings {

    static void insercion(String[] a) {
        for (int i = 1; i < a.length; i++) {
            String actual = a[i];
            int j = i - 1;
            while (j >= 0 && a[j].compareTo(actual) > 0) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = actual;
        }
    }

    public static void main(String[] args) {
        String[] a = {"pera", "manzana", "uva", "kiwi", "banana", "mango", "fresa"};
        System.out.println(Arrays.toString(a));
        insercion(a);
        System.out.println(Arrays.toString(a));
    }
}
