public class TestDriver {
    public static void main(String[] args){
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver Trouvée !");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver PostgreSQL manquant !");
            System.exit(1);
        }
    }
}
