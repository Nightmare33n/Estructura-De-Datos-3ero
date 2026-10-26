// Lista Doblemente Enlazada

package Practica6;

public class NodoListaD {
    public NodoListaD Izq;
    public NodoListaD Der;
    public Object Dato;

    public NodoListaD(NodoListaD Izq, Object Dato) {
        this.Izq = Izq;
        this.Dato = Dato;
        this.Der = null;
    }

    public NodoListaD(NodoListaD Izq, Object Dato, NodoListaD Der) {
        this.Izq = Izq;
        this.Dato = Dato;
        this.Der = Der;
    }

    public NodoListaD(Object Dato) {
        this(null, Dato);
    }

    @Override
    public String toString() {
        return String.valueOf(Dato);
    }
}
