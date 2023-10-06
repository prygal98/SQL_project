import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ApplicationEtudiant {

    String url = "jdbc:postgresql://localhost/postgres", user = "postgres", password=" ";

    private PreparedStatement connecterEtudiant,  visualiserCoursEtudiant, ajouterEtudiantGroupe, retirerEtudiantGroupe, visualiserProjetsCoursInscrit,
            visualiserProjetsSansGroupe, visualiserGroupeIncomplets;
    private Connection conn=null;
    private static int id;

    public ApplicationEtudiant() {



        // Try Driver
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Driver PostgreSQL manquant !"); System.exit(1);
        }

        // Try la connexion.
        try{
            conn = DriverManager.getConnection(url,user,password);
            System.out.println();
        }
        catch (SQLException e) { e.printStackTrace();
            System.out.println("Impossible de joindre le server !");
            System.exit(1);
        }

        // Try visualiser tous les cours de l'etudiant.
        try{
            visualiserCoursEtudiant = conn.prepareStatement("SELECT * FROM projet.visualiserCoursEtudiant WHERE  id_etudiant=? ;");
        }catch (Exception e){
            System.out.println("Erreur avec la requêtes SQL visualiser cours de l'etudiant !");
        }

        // Try ajouter un etudiant dans un groupe.
        try{
            ajouterEtudiantGroupe = conn.prepareStatement("SELECT projet.ajouterEtudiantGroupe(?, ?, ?);");
        }catch (Exception e){
            System.out.println("Erreur avec la requêtes SQL ajouterEtudiantGroupe !");
        }

        // Try Retirer un etudiant d'un groupe.
        try{
            retirerEtudiantGroupe = conn.prepareStatement("SELECT projet.retirerEtudiantGroupe(?, ?);");
        }catch (Exception e){
            System.out.println("Erreur avec la requêtes SQL retirerEtudiantGroupe !");
        }

        // Try Visualiser tous les projets des cours auxquels l'etudiant est inscrit.
        try{
            visualiserProjetsCoursInscrit = conn.prepareStatement("SELECT * FROM projet.visualiserProjetsCoursInscrit WHERE idetudiant=? ;");
        }catch (Exception e){
            System.out.println("Erreur avec la requêtes SQL visualiserProjetsCoursInscrit !");
        }

        // Try Visualiser tous les projets pour lesquels il n’a pas encore de groupe.
        try{
            visualiserProjetsSansGroupe = conn.prepareStatement("SELECT * FROM projet.visualiserProjetsSansGroupe WHERE idetudiant=? ;");
        }catch (Exception e){
            System.out.println("Erreur avec la requêtes SQL visualiserProjetsSansGroupe !");
        }

        // Try Visualiser toutes les compositions de groupes incomplets d’un projet
        try{
            visualiserGroupeIncomplets = conn.prepareStatement("SELECT * FROM projet.visualiserGroupeIncomplets WHERE identifiant_projet=? ;");
        }catch (Exception e){
            System.out.println("Erreur avec la requêtes SQL visualiserGroupeIncomplets !");
        }




    }

    /**
     * Connecte l'étudiant
     * @param mail
     * @param mot_de_passe
     */
    private void connecterEtudiant(String mail , String mot_de_passe) {
        try{
            try {
                PreparedStatement mdp =  conn.prepareStatement("SELECT mot_de_passe, id_etudiant FROM projet.etudiants WHERE mail = ?");
                mdp.setString(1, mail);
                ResultSet result = mdp.executeQuery();
                if (result.next()) {
                    String mdpString = result.getString("mot_de_passe");
                    if (!BCrypt.checkpw(mot_de_passe, mdpString)) {
                        throw new IllegalArgumentException("Invalid password provided");
                    }
                    id= result.getInt("id_etudiant");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e){
            System.out.println("aucun etudiant a ete trouve");
        }

    }

    /**
     * Visualiser tous les cours d'un étudiant
     */
    private void visualiserCoursEtudiant(){
        try {
            visualiserCoursEtudiant.setInt(1, id);
            try (ResultSet rs = visualiserCoursEtudiant.executeQuery()){
                while (rs.next()){
                    System.out.println(
                            "id-etudiant : " + rs.getString(1) +
                                    " | code-cours : " + rs.getString(2) +
                                    " | nom-cours : " + rs.getString(3) +
                                    " | listes des projets : " + rs.getString(4));
                }
            } catch (SQLException throwables) {
                System.out.println("Erreur avec la methode visualiserCoursEtudiant");
                throwables.printStackTrace();
            }
        }catch (SQLException e ){
            System.out.println("Erreur avec la methode visualiserCoursEtudiant");
            e.printStackTrace();
        }
    }

    /**
     * Ajouter un etudiant au groupe
     * @param idantifiant_projet
     * @param numero_groupe
     */
    private void ajouterEtudiantGroupe(String idantifiant_projet, int numero_groupe){
        try {
            ajouterEtudiantGroupe.setString(1, idantifiant_projet);
            ajouterEtudiantGroupe.setInt(2, numero_groupe);
            ajouterEtudiantGroupe.setInt(3,id);
            try (ResultSet rs = ajouterEtudiantGroupe.executeQuery()){
                System.out.println("La methode ajouter un étudiant dans un groupe a bien été éxecuté");
            }
        }catch (SQLException throwables){
            System.out.println("Erreur avec la methode ajouterEtudiantDansUnGroupe");
            throwables.printStackTrace();
        }
    }

    /**
     * Retirer un etudiant d'un groupe
     * @param idantifiant_projet
     */
    private void retirerEtudiantGroupe(String idantifiant_projet ){
        try {
            retirerEtudiantGroupe.setString(1, idantifiant_projet);
            retirerEtudiantGroupe.setInt(2,id);
            try (ResultSet rs = retirerEtudiantGroupe.executeQuery()){
                System.out.println("La methode Retirer un étudiant d'un groupe a bien été éxecuté");
            }
        }catch (SQLException throwables){
            System.out.println("Erreur avec la methode retirerEtudiantGroupe");
            throwables.printStackTrace();
        }
    }

    /**
     *
     */
    private void visualiserProjetsCoursInscrit(){
        try {
            visualiserProjetsCoursInscrit.setInt(1,id);
            try (ResultSet rs = visualiserProjetsCoursInscrit.executeQuery()){
                while (rs.next()){
                    System.out.println(
                            "identifiant-projet : " + rs.getString(2) +
                                    " | nom-projet: " + rs.getString(3) +
                                    " | identifiant-cours  : " + rs.getString(4) +
                                    " | numero-groupe : " + rs.getString(5));
                }
            } catch (SQLException throwables) {
                System.out.println("Erreur avec la methode visualiserProjetsCoursInscrit");
                throwables.printStackTrace();
            }
        }catch (SQLException e ){
            System.out.println("Erreur avec la methode visualiserProjetsCoursInscrit");
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void visualiserProjetsSansGroupe(){
        try {
            visualiserProjetsSansGroupe.setInt(1,id);
            try (ResultSet rs = visualiserProjetsSansGroupe.executeQuery()){
                while (rs.next()){
                    System.out.println(
                            "identifiant-projet : " + rs.getString(2) +
                                    " | nom-projet: " + rs.getString(3) +
                                    " | identifiant-cours  : " + rs.getString(4) +
                                    " | date-debut : " + rs.getString(5) +
                                    " | date-fin : " + rs.getString(6));
                }
            } catch (SQLException throwables) {
                System.out.println("Erreur avec la methode visualiserProjetsSansGroupe");
                throwables.printStackTrace();
            }
        }catch (SQLException e ){
            System.out.println("Erreur avec la methode visualiserProjetsSansGroupe");
            e.printStackTrace();
        }
    }


    /**
     * visualiser tous les groupes incomplets
     * @param identidiant
     */
    private void visualiserGroupeIncomplets(String identidiant){
        try {
            visualiserGroupeIncomplets.setString(1,identidiant);
            try (ResultSet rs = visualiserGroupeIncomplets.executeQuery()){
                while (rs.next()){
                    System.out.println(
                            "Numeéro : " + rs.getString(2) +
                                    " | nom : " + rs.getString(3) +
                                    " | prénom  : " + rs.getString(4) +
                                    " | Nombre de place disponible : " + rs.getString(5));
                }
            } catch (SQLException throwables) {
                System.out.println("Erreur avec la methode visualiserGroupeIncomplets");
                throwables.printStackTrace();
            }
        }catch (SQLException e ){
            System.out.println("Erreur avec la methode visualiserGroupeIncomplets");
            e.printStackTrace();
        }
    }




    /**
     * Termine la connexion
     */
    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Faire un try catch dans chaque if -> pas sur de l'utilité à 100%
    public static void main(String[] args){

        ApplicationEtudiant applicationEtudiant = new ApplicationEtudiant();
        Scanner scan = new Scanner(System.in);


        System.out.println("Bonjour, Veillez vous connecter :  ");

        try {
            System.out.println("Introduire votre adresse mail vinci : ");
            String mail = scan.nextLine();
            System.out.println("Introduire votre mot de passe : ");
            String password = scan.nextLine();
            applicationEtudiant.connecterEtudiant(mail,password);
            System.out.println("votre identifiant etudiant est le numero "+id);
            if(id == 0){
                System.exit(1);
            }
        }catch (Exception e) {
            System.out.println("Erreur avec les requêtes SQL !");
            System.exit(1);
        }


        int value;
        do {
            System.out.println(" ");
            System.out.println("1) Visualiser tous les cours de l'etudiant  ");
            System.out.println("2) Ajouter l'etudiant dans un groupe  ");
            System.out.println("3) Retirer l'etudiant d'un groupe  ");
            System.out.println("4) Visualiser tous les projets des cours auxquels il est inscrit ");
            System.out.println("5) Visualiser tous les projets pour lesquelles l etudiant n a pas encore de groupe");
            System.out.println("6) Visualiser toutes les compositions de groupes incomplets d’un projet  ");
            System.out.println(" ");
            System.out.println("Choisir un numero entre 1 et 6 ");
            System.out.println("--------------------------------");
            System.out.println(" ");
            value = Integer.parseInt(scan.nextLine());
            try{
                if (value == 1){
                    applicationEtudiant.visualiserCoursEtudiant();
                }
                if (value == 2){
                    System.out.println("veuillez introduire l'identifiant du projet : ");
                    String idantifiant = scan.nextLine();
                    System.out.println("veuillez introduire le numero du groupe : ");
                    int numero = Integer.parseInt(scan.nextLine());
                    applicationEtudiant.ajouterEtudiantGroupe(idantifiant,numero);
                }
                if (value == 3){
                    System.out.println("veuillez introduire l'identifiant du projet dont vous voulez retirer l'etudiant : ");
                    String idantifiant = scan.nextLine();
                    applicationEtudiant.retirerEtudiantGroupe(idantifiant);
                }
                if (value == 4 ){
                    applicationEtudiant.visualiserProjetsCoursInscrit();
                }
                if (value == 5){
                    applicationEtudiant.visualiserProjetsSansGroupe();
                }
                if (value == 6){
                    System.out.println("veuillez introduire l'identifiant du projet dont vous voulez voir la composition des groupes incomplets : ");
                    String idantifiant = scan.nextLine();
                    applicationEtudiant.visualiserGroupeIncomplets(idantifiant);
                }
            }catch (Exception e) {
                System.out.println("Erreur avec les requêtes SQL !");
                System.exit(1);
            }
        }while(value >=1 && value <= 6 );
        applicationEtudiant.close();
    }

}