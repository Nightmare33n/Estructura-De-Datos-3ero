import javax.swing.JOptionPane;

public class VectoresJOptionPane {

    public static void main(String[] args) {

        // 1. Pedir el tamaño de los vectores
        int n = Integer.parseInt(
            JOptionPane.showInputDialog("¿De cuántos elementos serán los vectores?")
        );

        int[] vectorA = new int[n];
        int[] vectorB = new int[n];

        // 2. Llenar vector A
        for (int i = 0; i < n; i++) {
            vectorA[i] = Integer.parseInt(
                JOptionPane.showInputDialog("Elemento A[" + i + "]:")
            );
        }

        // 3. Llenar vector B
        for (int i = 0; i < n; i++) {
            vectorB[i] = Integer.parseInt(
                JOptionPane.showInputDialog("Elemento B[" + i + "]:")
            );
        }

        // 4. Suma de vectores (elemento por elemento)
        int[] suma = new int[n];
        for (int i = 0; i < n; i++) {
            suma[i] = vectorA[i] + vectorB[i];
        }

        // 5. Producto punto (multiplicación de vectores -> escalar)
        int productoPunto = 0;
        for (int i = 0; i < n; i++) {
            productoPunto += vectorA[i] * vectorB[i];
        }

        // 6. Multiplicación elemento por elemento (por si la quieres también)
        int[] multiplicacionElementoAElemento = new int[n];
        for (int i = 0; i < n; i++) {
            multiplicacionElementoAElemento[i] = vectorA[i] * vectorB[i];
        }

        // 7. Armar resultados en texto
        StringBuilder resultado = new StringBuilder();
        resultado.append("Vector A: ").append(arrayToString(vectorA)).append("\n");
        resultado.append("Vector B: ").append(arrayToString(vectorB)).append("\n\n");
        resultado.append("Suma (A + B): ").append(arrayToString(suma)).append("\n\n");
        resultado.append("Multiplicación elemento a elemento: ")
                 .append(arrayToString(multiplicacionElementoAElemento)).append("\n\n");
        resultado.append("Producto punto (A · B): ").append(productoPunto);

        // 8. Mostrar resultado
        JOptionPane.showMessageDialog(null, resultado.toString(), "Resultados", JOptionPane.INFORMATION_MESSAGE);
    }

    // Método auxiliar para mostrar arrays bonitos
    private static String arrayToString(int[] vector) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            sb.append(vector[i]);
            if (i < vector.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}