import javax.swing.JOptionPane;

public class Practica2_EDD_S {
    public static void main(String[] args) {
        int suma = 0;
        int i = 0;
        int n = Integer.parseInt(JOptionPane.showInputDialog("Cuantos datos deseas ingresar?: "));
        int datos[] = new int[n];
        
        do {
            datos[i] = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el dato - " + (i + 1)));
            i++;
        } while (i < n);

        int contador_positivos  = 0;

        for (int j = 0; j < n; j++) {
            if (datos[j] > 0) {
                suma += datos[j];
                contador_positivos++;
            }
        }
        float promedio = (float) suma / contador_positivos;
        System.out.println("El promedio de los positivos es: " + promedio);

    }
}
