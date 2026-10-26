
import java.util.Scanner;

public class Examen {

    // Metodo recursivo: imprime n y luego los numeros menores hasta llegar a 0
    public static void Cuenta(int n) {
        if (n <= 0) { // al llegar a 0 ya no se imprime nada
            return;
        }
        System.out.print(n); 
        if (n > 1) {
            System.out.print(", ");
        }
        Cuenta(n - 1); // vuelve a llamar a la misma funcion pero con el numero anterior
    }
  
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int numero;

        do { // bucle do while para imprimir los numeros en forma de lista usando la coma del metodo
            System.out.print("Mete un numero positivo: "); // se pide el numero 
            numero = sc.nextInt(); // se ingresa el valor y se guarda en numero
            if (numero <= 0) {
                System.out.println("Mete un numero positivo");
            }
        } while (numero <= 0);

        System.out.print(" "); // imprime la lista de numeros con la coma
        Cuenta(numero);
        System.out.println();

        sc.close();
    }


}
