import java.text.Normalizer;
import java.util.Scanner;

public class Practica11 {

    // Pila implementada con arreglo
    static class Pila {
        private char[] datos;
        private int tope = -1;

        Pila(int capacidad) {
            datos = new char[capacidad];
        }

        void push(char c) {
            datos[++tope] = c;
        }

        char pop() {
            return datos[tope--];
        }

        boolean estaVacia() {
            return tope == -1;
        }
    }

    // Quita acentos, espacios y signos; deja solo letras/dígitos en minúscula
    static String normalizar(String frase) {
        String sinAcentos = Normalizer.normalize(frase, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sinAcentos.toLowerCase().replaceAll("[^a-z0-9]", "");
    }

    static boolean esPalindromo(String frase) {
        String limpia = normalizar(frase);
        Pila pila = new Pila(limpia.length());

        for (int i = 0; i < limpia.length(); i++) {
            pila.push(limpia.charAt(i));
        }

        // Al sacar de la pila se obtiene la frase invertida
        for (int i = 0; i < limpia.length(); i++) {
            if (limpia.charAt(i) != pila.pop()) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese una frase: ");
        String frase = sc.nextLine();

        if (esPalindromo(frase)) {
            System.out.println("\"" + frase + "\" SÍ es un palíndromo.");
        } else {
            System.out.println("\"" + frase + "\" NO es un palíndromo.");
        }
        sc.close();
    }
}
