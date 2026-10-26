import java.util.Scanner;

public class Practica12 {

    // Pila de caracteres implementada con arreglo
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

        char peek() {
            return datos[tope];
        }

        boolean estaVacia() {
            return tope == -1;
        }
    }

    static int prioridad(char op) {
        switch (op) {
            case '^': return 3;
            case '*':
            case '/': return 2;
            case '+':
            case '-': return 1;
            default:  return 0; // '('
        }
    }

    static boolean esOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    static String infijaAPostfija(String infija) {
        Pila pila = new Pila(infija.length());
        StringBuilder postfija = new StringBuilder();

        for (int i = 0; i < infija.length(); i++) {
            char c = infija.charAt(i);

            if (Character.isWhitespace(c)) {
                continue;
            } else if (Character.isLetterOrDigit(c)) {
                postfija.append(c);
            } else if (c == '(') {
                pila.push(c);
            } else if (c == ')') {
                while (!pila.estaVacia() && pila.peek() != '(') {
                    postfija.append(pila.pop());
                }
                if (pila.estaVacia()) {
                    throw new IllegalArgumentException("Paréntesis desbalanceados");
                }
                pila.pop(); // descarta '('
            } else if (esOperador(c)) {
                // '^' es asociativo por la derecha, los demás por la izquierda
                while (!pila.estaVacia() && pila.peek() != '('
                        && (prioridad(pila.peek()) > prioridad(c)
                        || (prioridad(pila.peek()) == prioridad(c) && c != '^'))) {
                    postfija.append(pila.pop());
                }
                pila.push(c);
            } else {
                throw new IllegalArgumentException("Carácter no válido: " + c);
            }
        }

        while (!pila.estaVacia()) {
            char c = pila.pop();
            if (c == '(') {
                throw new IllegalArgumentException("Paréntesis desbalanceados");
            }
            postfija.append(c);
        }
        return postfija.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese la expresión infija: ");
        String expresion = sc.nextLine();

        try {
            System.out.println("Postfija: " + infijaAPostfija(expresion));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        sc.close();
    }
}
