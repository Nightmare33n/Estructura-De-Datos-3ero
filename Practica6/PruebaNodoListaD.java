package Practica6;

import javax.swing.JOptionPane;

public class PruebaNodoListaD {
    public static void main(String[] args) {
        NodoListaD inicio = null;
        NodoListaD fin = null;

        while (true) {
            String entrada = JOptionPane.showInputDialog(null,
                    "Ingresa un dato para la lista (Cancelar para terminar):");
            if (entrada == null) break;

            NodoListaD nuevo = new NodoListaD(fin, entrada.trim());
            if (fin == null) {
                inicio = nuevo;
            } else {
                fin.Der = nuevo;
            }
            fin = nuevo;
        }

        if (inicio == null) {
            JOptionPane.showMessageDialog(null, "La lista está vacía.");
            return;
        }

        StringBuilder adelante = new StringBuilder();
        for (NodoListaD p = inicio; p != null; p = p.Der) {
            adelante.append(p.Dato).append(p.Der != null ? " <-> " : "");
        }

        StringBuilder atras = new StringBuilder();
        for (NodoListaD p = fin; p != null; p = p.Izq) {
            atras.append(p.Dato).append(p.Izq != null ? " <-> " : "");
        }

        JOptionPane.showMessageDialog(null,
                "Izquierda a derecha:\n" + adelante + "\n\nDerecha a izquierda:\n" + atras);
    }
}
