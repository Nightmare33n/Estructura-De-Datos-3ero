package ListasEnlazadas;

import java.util.LinkedList;
import javax.swing.JOptionPane;

public class ListaEnlazada {
    public static void main(String[] args) {
        LinkedList<String> lista1 = new LinkedList<>();
        int numElementos = Integer.parseInt(JOptionPane.showInputDialog("Cuántos elementos quieres meter: "));

        for (int i = 0; i < numElementos; i++) {
            String elemento = JOptionPane.showInputDialog("Elemento a ingresar: ");
            lista1.add(elemento);
        }

        if (!lista1.isEmpty()) {
            lista1.set(0, "Elemento modificado");
        }
        JOptionPane.showMessageDialog(null, "Elementos de la lista: " + lista1);

        JOptionPane.showMessageDialog(null, lista1.indexOf(1));
       
    }
}