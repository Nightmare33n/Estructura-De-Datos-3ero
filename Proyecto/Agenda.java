package Proyecto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Agenda {
    static List<String> contactos = new ArrayList<>();
    static DefaultListModel<String> modelo = new DefaultListModel<>();
    static JList<String> lista = new JList<>(modelo);
    static JTextField busqueda = new JTextField();
    static JTextField nombre = new JTextField();
    static JTextField telefono = new JTextField();

    static void quicksort(List<String> l, int lo, int hi) {
        if (lo >= hi) return;
        String pivote = l.get((lo + hi) / 2);
        int i = lo, j = hi;
        while (i <= j) {
            while (l.get(i).compareToIgnoreCase(pivote) < 0) i++;
            while (l.get(j).compareToIgnoreCase(pivote) > 0) j--;
            if (i <= j) Collections.swap(l, i++, j--);
        }
        quicksort(l, lo, j);
        quicksort(l, i, hi);
    }

    static int busquedaBinaria(String prefijo) {
        int lo = 0, hi = contactos.size();
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (contactos.get(mid).compareToIgnoreCase(prefijo) < 0) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    static void mostrar() {
        String q = busqueda.getText().toLowerCase();
        modelo.clear();
        if (q.matches("[0-9+\\- ]*[0-9][0-9+\\- ]*")) {
            for (String c : contactos)
                if (c.substring(c.lastIndexOf(" - ") + 3).contains(q)) modelo.addElement(c);
            return;
        }
        for (int i = busquedaBinaria(q); i < contactos.size() && contactos.get(i).toLowerCase().startsWith(q); i++)
            modelo.addElement(contactos.get(i));
    }

    static void guardar() {
        if (nombre.getText().isBlank()) return;
        contactos.add(nombre.getText().trim() + " - " + telefono.getText().trim());
        quicksort(contactos, 0, contactos.size() - 1);
        nombre.setText("");
        telefono.setText("");
        mostrar();
    }

    static void editar() {
        String seleccionado = lista.getSelectedValue();
        if (seleccionado == null) return;
        contactos.remove(seleccionado);
        guardar();
    }

    static void borrar() {
        contactos.remove(lista.getSelectedValue());
        nombre.setText("");
        telefono.setText("");
        mostrar();
    }

    static void cargarCampos() {
        String s = lista.getSelectedValue();
        if (s == null) return;
        int i = s.lastIndexOf(" - ");
        nombre.setText(s.substring(0, i));
        telefono.setText(s.substring(i + 3));
    }

    static JButton boton(String texto, Runnable accion) {
        JButton b = new JButton(texto);
        b.addActionListener(e -> accion.run());
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("Agenda telefónica");
            ventana.setLayout(new BorderLayout(5, 5));

            JPanel arriba = new JPanel(new BorderLayout(5, 5));
            arriba.add(new JLabel(" Buscar:"), BorderLayout.WEST);
            arriba.add(busqueda);

            JPanel datos = new JPanel(new GridLayout(1, 4, 5, 5));
            datos.add(new JLabel(" Nombre:"));
            datos.add(nombre);
            datos.add(new JLabel(" Teléfono:"));
            datos.add(telefono);

            JPanel botones = new JPanel(new GridLayout(1, 3, 5, 5));
            botones.add(boton("Agregar", Agenda::guardar));
            botones.add(boton("Editar", Agenda::editar));
            botones.add(boton("Borrar", Agenda::borrar));

            JPanel abajo = new JPanel(new GridLayout(2, 1, 5, 5));
            abajo.add(datos);
            abajo.add(botones);

            busqueda.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) { mostrar(); }
            });
            lista.addListSelectionListener(e -> cargarCampos());

            ventana.add(arriba, BorderLayout.NORTH);
            ventana.add(new JScrollPane(lista), BorderLayout.CENTER);
            ventana.add(abajo, BorderLayout.SOUTH);
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setSize(480, 400);
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }
}