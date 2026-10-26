public class Menu {
    public static void main(String[] args) {
        int n = 100;
        long a = 0, b = 1;
        for(int i = 0;i < n;i++){
            System.out.println(a + " ");
            long temp = a + b;
            a = b;
            b = temp;
        }
    }
}
