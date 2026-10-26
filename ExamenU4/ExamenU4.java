package ExamenU4;

import java.util.Arrays;
import javax.swing.JOptionPane;

public class ExamenU4 {
    // Metodo busqueda secuencial
    static int busquedaSecuencial(String[] nombres, String buscado) {
        for(int i = 0; i < nombres.length; i++){
            if(nombres[i].equalsIgnoreCase(buscado)) {
                return i;
            }
        }
        return -1;
    }
    public static void main(String[] args) {
        String[] nombres = new String[10];
            for (int i = 0; i < nombres.length; i++) {
                String entrada = JOptionPane.showInputDialog(null, "Ingresa el nombre " + (i + 1));
                nombres[i] = entrada.trim();
            }

        String buscado = JOptionPane.showInputDialog(null, "Ingrese el nombre a buscar:");
        if (buscado == null) return;
        buscado = buscado.trim();
        int pos = busquedaSecuencial(nombres, buscado);
        if (pos >= 0) {
            JOptionPane.showMessageDialog(null,   buscado + " encontrado en posicion " + pos);
        } else {
            JOptionPane.showMessageDialog(null,   buscado + " no existe en el array");
        }
    }
}
