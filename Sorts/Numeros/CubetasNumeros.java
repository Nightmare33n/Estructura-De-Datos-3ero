package Sorts.Numeros;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Bucket sort (cubetas): repartes los numeros en cubetas segun su rango,
// ordenas cada cubeta (con insercion) y las juntas en orden
public class CubetasNumeros {

    static void cubetas(int[] a) {
        int n = a.length;
        if (n == 0) return;
        int min = a[0], max = a[0];
        for (int x : a) {
            if (x < min) min = x;
            if (x > max) max = x;
        }

        List<List<Integer>> cubetas = new ArrayList<>();
        for (int i = 0; i < n; i++) cubetas.add(new ArrayList<>());

        // a que cubeta va cada quien (long pa' que no se desborde)
        long rango = (long) max - min + 1;
        for (int x : a) {
            int idx = (int) (((long) x - min) * n / rango);
            cubetas.get(idx).add(x);
        }

        int k = 0;
        for (List<Integer> c : cubetas) {
            insercion(c);
            for (int x : c) a[k++] = x;
        }
    }

    // insercion normalita pero sobre la lista de la cubeta
    static void insercion(List<Integer> c) {
        for (int i = 1; i < c.size(); i++) {
            int actual = c.get(i);
            int j = i - 1;
            while (j >= 0 && c.get(j) > actual) {
                c.set(j + 1, c.get(j));
                j--;
            }
            c.set(j + 1, actual);
        }
    }

    public static void main(String[] args) {
        int[] a = {29, 3, 72, 1, 45, 8, 16};
        System.out.println(Arrays.toString(a));
        cubetas(a);
        System.out.println(Arrays.toString(a));
    }
}
