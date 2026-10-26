package Sorts.Letras;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Bucket sort con letras: repartes las letras en cubetas segun su rango (a-e, f-j, etc.),
// ordenas cada cubeta y las juntas
public class CubetasLetras {

    static void cubetas(char[] a) {
        int n = a.length;
        if (n == 0) return;
        char min = a[0], max = a[0];
        for (char c : a) {
            if (c < min) min = c;
            if (c > max) max = c;
        }

        List<List<Character>> cubetas = new ArrayList<>();
        for (int i = 0; i < n; i++) cubetas.add(new ArrayList<>());

        int rango = max - min + 1;
        for (char c : a) cubetas.get((c - min) * n / rango).add(c);

        int k = 0;
        for (List<Character> cub : cubetas) {
            insercion(cub);
            for (char c : cub) a[k++] = c;
        }
    }

    static void insercion(List<Character> c) {
        for (int i = 1; i < c.size(); i++) {
            char actual = c.get(i);
            int j = i - 1;
            while (j >= 0 && c.get(j) > actual) {
                c.set(j + 1, c.get(j));
                j--;
            }
            c.set(j + 1, actual);
        }
    }

    public static void main(String[] args) {
        char[] a = {'m', 'z', 'c', 'a', 'q', 'f', 'b'};
        System.out.println(Arrays.toString(a));
        cubetas(a);
        System.out.println(Arrays.toString(a));
    }
}
