package Sorts.Strings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Bucket sort con Strings: una cubeta por primera letra (todas las de 'a' juntas, las de 'b'...),
// ordenas cada cubeta con insercion y las pegas en orden
public class CubetasStrings {

    static final int R = 256; // ASCII extendido

    static void cubetas(String[] a) {
        // cubeta 0 = String vacio, las demas = codigo de la primera letra + 1
        List<List<String>> cubetas = new ArrayList<>();
        for (int i = 0; i <= R; i++) cubetas.add(new ArrayList<>());

        for (String s : a) {
            int idx = s.isEmpty() ? 0 : s.charAt(0) + 1;
            cubetas.get(idx).add(s);
        }

        int k = 0;
        for (List<String> c : cubetas) {
            insercion(c);
            for (String s : c) a[k++] = s;
        }
    }

    static void insercion(List<String> c) {
        for (int i = 1; i < c.size(); i++) {
            String actual = c.get(i);
            int j = i - 1;
            while (j >= 0 && c.get(j).compareTo(actual) > 0) {
                c.set(j + 1, c.get(j));
                j--;
            }
            c.set(j + 1, actual);
        }
    }

    public static void main(String[] args) {
        String[] a = {"pera", "manzana", "uva", "kiwi", "banana", "mango", "fresa"};
        System.out.println(Arrays.toString(a));
        cubetas(a);
        System.out.println(Arrays.toString(a));
    }
}
