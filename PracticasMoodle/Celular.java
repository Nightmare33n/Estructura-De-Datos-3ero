public class Celular {

    // Atributos
    private String nombre;
    private String color;

    // Constructor
    public Celular(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
    }

    public void llamar() {
        System.out.println("Llamando de " + nombre + " Color: " + color);
    }

    public void detalleCelular() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("Nombre: " + nombre + "\nColor:  " + this.color);
        System.out.println("-----------------------------------------------------------");
    }

    public static void main(String[] args) {
        Celular celular1 = new Celular("LG", "Rojo");

        celular1.detalleCelular();
        celular1.llamar();

        Celular celular2 = new Celular("MOTOROLA", "VERDE");
        celular2.llamar();
    }
}
