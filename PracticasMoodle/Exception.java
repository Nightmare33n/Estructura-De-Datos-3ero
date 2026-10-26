public class Exception {
    public static void main(String [] args) {
        String text1 = "Hola";
        String text2 = "Adios";
        try{
            System.out.println(text1/text2);
        }catch(Exception e) {
            System.out.println(e);
        }
        
        System.out.println("Saliste del programa");
    }
}