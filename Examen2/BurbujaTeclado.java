import java.util.Arrays;

import javax.swing.JOptionPane;

public class BurbujaTeclado {

    static void burbuja(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
           
            for (int j = 0; j < a.length - 1-i; j++) {
                if (a[j] > a[j + 1]) {
                    int tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                }
            }
        }
    }

    static void burbujaMayorAMenor(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - 1 - i; j++) {
           
                if (a[j] < a[j + 1]) {
                    int tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int cuantos = 10;
        int[] a = new int[cuantos]; 
        for (int i = 0; i < cuantos; i++) { 
            a[i] = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el valor " + i + " del arreglo"));
        }
        System.out.println("Arreglo 1");
        System.out.println(Arrays.toString(a));
        burbuja(a);
        System.out.println("ordenado x bubble sort");
        System.out.println(Arrays.toString(a));

        burbujaMayorAMenor(a);
        System.out.println("mayor a menor");
        System.out.println(Arrays.toString(a));
    }
}
