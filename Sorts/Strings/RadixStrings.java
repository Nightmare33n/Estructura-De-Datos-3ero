package Sorts.Strings;
import java.util.Arrays;

// Radix (LSD) con Strings: es counting sort aplicado letra por letra, empezando por la ULTIMA posicion.
// (por eso no hay "ConteoStrings": contar palabras completas no tiene sentido, esto es su version)
// Si una palabra es mas corta, en esa posicion cuenta como "vacio", que va antes que cualquier letra
public class RadixStrings {

    static final int R = 256; // ASCII extendido (la ñ y los acentos caben)

    static void radix(String[] a) {
        int maxLen = 0;
        for (String s : a) if (s.length() > maxLen) maxLen = s.length();

        for (int d = maxLen - 1; d >= 0; d--) {
            // +1 porque la cubeta 0 es para "ya no hay letra aqui"
            int[] cuenta = new int[R + 1];
            String[] salida = new String[a.length];
            for (String s : a) cuenta[letraEn(s, d)]++;
            for (int i = 1; i <= R; i++) cuenta[i] += cuenta[i - 1];
            // de atras pa' delante para que sea estable
            for (int i = a.length - 1; i >= 0; i--) {
                salida[--cuenta[letraEn(a[i], d)]] = a[i];
            }
            System.arraycopy(salida, 0, a, 0, a.length);
        }
    }

    // la letra en la posicion d (corrida +1), o 0 si la palabra ya se acabo
    static int letraEn(String s, int d) {
        return d < s.length() ? s.charAt(d) + 1 : 0;
    }

    public static void main(String[] args) {
        String[] a = {"pera", "manzana", "uva", "kiwi", "banana", "mango", "fresa"};
        System.out.println(Arrays.toString(a));
        radix(a);
        System.out.println(Arrays.toString(a));
    }
}
