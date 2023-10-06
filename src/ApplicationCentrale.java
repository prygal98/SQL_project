import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;



public class ApplicationCentrale {

    private static Connection conn = null;
    String url = "jdbc:postgresql://localhost/postgres", user = "postgres", password=" ";

    private PreparedStatement ajouterCours, ajouterEtudiant, ajouterEtudiantCours, ajouterProjetCours, ajouterGroupesProjets
            , visualiserLesCours, visualiserLesProjets, visualiserCompositionsGroupes, validerGroupe, validerLesGroupes;


    public ApplicationCentrale() {
        // Try Driver
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Driver PostgreSQL manquant !");
            System.exit(1);
        }

        // Try la connexion.
        try{
            conn = DriverManager.getConnection(url,user,password);
            System.out.println();
        }
        catch (SQLException e) {
            System.out.println("Impossible de joindre le server !");
            System.exit(1);
        }
        // Try ajouter cours.
        try{
            ajouterCours = conn.prepareStatement("SELECT projet.ajouterCours( ?,?,?,?);");
        }catch (Exception e){
            System.out.println("Erreur avec la requêtes SQL ajouterCours !");
        }

        // Try ajouter un etudiant.
        try {
            ajouterEtudiant = conn.prepareStatement("SELECT projet.ajouterEtudiant(?,?,?,?);");
        }catch (Exception e){
            System.out.println("Erreur avec la requêtes SQL ajouterEtudiant !");
        }

        // Try ajouter un etudiant à un cours.
        try{
            ajouterEtudiantCours = conn.prepareStatement("SELECT projet.ajouterEtudiantAUnCours(?, ?);");
        }catch (Exception e){
            System.out.println("Erreur avec la requêtes SQL ajouterEtudiantCours !");
        }

        // Try ajouter un projet à un cours.
        try {
            ajouterProjetCours = conn.prepareStatement("SELECT projet.ajouterProjet(?,?,?,?,?);");
        }catch (Exception e){
            System.out.println("Erreur avec la requêtes SQL ajouterProjetCours !");
        }

        // Try ajouter des groupes à un projet.
        try {
            ajouterGroupesProjets = conn.prepareStatement("SELECT projet.creerGroupe(?,?,?);");
        }catch (Exception e){
            System.out.println("Erreur avec la requêtes SQL ajouterGroupesProjets !");
        }

        // Try visualiser tous les cours
        try {
            visualiserLesCours = conn.prepareStatement("SELECT * FROM projet.tousLesCours;");
        }catch (Exception e){
            System.out.println("Erreur avec la requêtes SQL visualiserLesCours !");
        }

        // Try visualiser tous les porjets
        try {
            visualiserLesProjets = conn.prepareStatement("SELECT * FROM projet.visualiserProjet;");
        }catch (Exception e){
            System.out.println("Erreur avec la requêtes SQL visualiserLesProjets !");
        }

        // Try Visualiser toutes les compositions de groupe d’un projet
        try {
            visualiserCompositionsGroupes = conn.prepareStatement("SELECT numero, nom, prenom, est_complet, valide " +
                    "FROM projet.visualiserToutesLesCompositionsDuProjet WHERE id_projet = ?;");
        }catch (Exception e){
            System.out.println("Erreur avec la requêtes SQL visualiserCompositionsGroupes !");
        }

        // Try Valider un groupe
        try {
            validerGroupe = conn.prepareStatement("SELECT projet.validerGroupe(?, ?);");
        } catch (Exception e){
            System.out.println("Erreur avec la requêtes SQL validerGroupe !");
        }

        // Try Valider tous les groupes d’un projet
        try {
            validerLesGroupes = conn.prepareStatement("SELECT projet.validerTousLesGroupes(?);");
        }catch (Exception e){
            System.out.println("Erreur avec la requêtes SQL validerTousLesGroupes !");
        }



    }

    /** 1
     * Ajoute un nouveau cours.
     * @param code_cour
     * @param nom
     * @param bloc
     * @param nombreCredits
     */
    private void ajouterUnCours(String code_cour, String nom , String bloc , int  nombreCredits){
        try{
            ajouterCours.setString(1, code_cour);
            ajouterCours.setString(2, nom);
            ajouterCours.setString(3, bloc);
            ajouterCours.setInt(4, nombreCredits);
            try (ResultSet rs = ajouterCours.executeQuery()) {
                System.out.println("La methode ajouter un cours à bien été éxecuté.");
            }
        } catch (SQLException throwables) {
            System.out.println("Erreur avec la methode ajouterUnCours");
            StackTraceElement[] stackTrace = throwables.getStackTrace();
            if (stackTrace.length > 0) {
                System.out.println(stackTrace[0].toString());
            }
        }
    }

    /** 2
     * Ajoute un étudiant
     * @param nom
     * @param prenom
     * @param mail
     * @param mdp
     */
    private void ajouterUnEtudiant(String nom, String prenom, String mail, String mdp){
        try {
            ajouterEtudiant.setString(1,nom);
            ajouterEtudiant.setString(2,prenom);
            ajouterEtudiant.setString(3,mail);
            ajouterEtudiant.setString(4,mdp);
            try(ResultSet rs = ajouterEtudiant.executeQuery()){
                System.out.println("La methode ajouter un étudiant à bien été éxecuté.");
            }
        }catch (SQLException throwables) {
            System.out.println("Erreur avec la methode ajouterUnEtudiant");
            throwables.printStackTrace(); // reprendre juste la premiere ligne de lexception et pas tous afficher;
        }
    }

    /** 3
     * Ajoute un étudiant à un cours
     * @param mail
     * @param identifiant_cours
     */
    private void ajouterEtudiantAUnCours(String mail , String identifiant_cours){
        try {
            ajouterEtudiantCours.setString(1, mail);
            ajouterEtudiantCours.setString(2, identifiant_cours);
            try (ResultSet rs = ajouterEtudiantCours.executeQuery()){
                System.out.println("La methode ajouter un étudiant à un cours a bien été éxecuté");
            }
        }catch (SQLException throwables){
            System.out.println("Erreur avec la methode ajouterEtudiantAUnCours");
            throwables.printStackTrace(); // reprendre juste la premiere ligne de lexception et pas tous afficher;
        }
    }

    /** 4
     * Ajoute un projet dans un groupe donnée
     * @param nom
     * @param date_debut
     * @param date_fin
     * @param identifiant_projet
     * @param id_cour
     */
    private void ajouterUnProjetAUnCours(String nom, Date date_debut, Date date_fin, String identifiant_projet, int id_cour){
        try {
            ajouterProjetCours.setString(1,nom);
            ajouterProjetCours.setDate(2, new java.sql.Date(date_debut.getTime()));
            ajouterProjetCours.setDate(3, new java.sql.Date(date_fin.getTime()));
            ajouterProjetCours.setString(4,identifiant_projet);
            ajouterProjetCours.setInt(5,id_cour);
            try (ResultSet rs = ajouterProjetCours.executeQuery()){
                System.out.println("La methode ajouter un projet à un cours a bien été éxecuté");
            }
        }catch (SQLException throwables){
            System.out.println("Erreur avec la methode ajouterUnProjetAUnCours");
            throwables.printStackTrace(); // reprendre juste la premiere ligne de lexception et pas tous afficher;
        }
    }

    // FONCTIONNE PAS
    /** 5
     * @param identifiant_projet
     * @param nombre_de_groupes
     * @param nombres_de_places
     */
    private void ajouterDesGroupesAUnProjet(String identifiant_projet, int nombre_de_groupes, int nombres_de_places){
        try {
            ajouterGroupesProjets.setString(1, identifiant_projet);
            ajouterGroupesProjets.setInt(2, nombre_de_groupes);
            ajouterGroupesProjets.setInt(3,nombres_de_places);
            try (ResultSet rs = ajouterGroupesProjets.executeQuery()){
                System.out.println("La methode ajouter des droupes à un projet à bien été éxecuté. ");
            }
        }catch (SQLException throwables){
            System.out.println("Erreur avec la methode ajouterDesGroupesAUnProjet");
            throwables.printStackTrace(); // reprendre juste la premiere ligne de lexception et pas tous afficher;
        }
    }

    /**6
     *
     */
    private void visualiserTousLesCours(){
        try (ResultSet rs = visualiserLesCours.executeQuery()){
            while (rs.next()){
                System.out.println(
                        "code_cour : " + rs.getString(1) +
                                " | nom : " + rs.getString(2) +
                                " | listes des projets : " + rs.getString(3));
            }
        } catch (SQLException throwables) {
            System.out.println("Erreur avec la methode visualiserTousLesCours");
            throwables.printStackTrace();
        }
    }

    /**7
     *
     */
    private void visualiserTousLesProjets(){
        try (ResultSet rs = visualiserLesProjets.executeQuery()) {
            while (rs.next()){
                System.out.println(
                        "id projet : " + rs.getString(1) +
                                " | nom : " + rs.getString(2) +
                                " | id cours : " + rs.getString(3) +
                                " | nombres de groupes : " + rs.getString(4) +
                                " | nombres de groupes complets : " + rs.getString(5) +
                                " | nombres de groupes valides : " + rs.getString(6));
            }
        } catch (SQLException throwables) {
            System.out.println("Erreur avec la methode visualiserTousLesProjets");
            throwables.printStackTrace();
        }
    }

    /**8
     *
     * @param id_projet
     */
    private void visualiserTousLesCompositionsDunProjet(int id_projet){
        try {
            visualiserCompositionsGroupes.setInt(1, id_projet);
            try (ResultSet rs = visualiserCompositionsGroupes.executeQuery()){
                while (rs.next()){
                    System.out.println(
                            "numero : " + rs.getString(1) +
                                    " | nom : " + rs.getString(2)  +
                                    " | prenom : " + rs.getString(3) +
                                    " | est complet : " + (rs.getString(4).equals("t") ? rs.getString(4).replace("t", "true") : rs.getString(4).replace("f", "false")) +
                                    " | est valise : " + (rs.getString(5).equals("t") ? rs.getString(5).replace("t", "true") : rs.getString(5).replace("f", "false")));
                }
            } catch (SQLException throwables) {
                System.out.println("Erreur avec la methode visualiserTousLesCompositionsDunProjet");
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 9
     * @param identifiant_projet
     * @param num_groupe
     */
    private void validerUnGroupe(String identifiant_projet , int num_groupe){
        try {
            validerGroupe.setString(1, identifiant_projet);
            validerGroupe.setInt(2,num_groupe);
            try (ResultSet rs = validerGroupe.executeQuery()) {
                System.out.println("La méthode valider groupe a bien été éxécuté");
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 10
     * @param identifiant_projet
     */
    private void validerTousLesGroupes(String identifiant_projet){
        try {
            validerLesGroupes.setString(1, identifiant_projet);
            try (ResultSet rs = validerLesGroupes.executeQuery()) {
                System.out.println("La méthode valider groupe a bien été éxécuté");
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
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
        ApplicationCentrale applicationEtudiant = new ApplicationCentrale();
        String salt = BCrypt.gensalt();;
        Scanner scan = new Scanner(System.in);
        int value ;
        do{
            System.out.println("1) creer un cours ");
            System.out.println("2) creer un étudiant ");
            System.out.println("3) inscrire un étudiant à un cours ");
            System.out.println("4) creer un projet à un cours ");
            System.out.println("5) creer des groupes à un projet ");
            System.out.println("6) visualiser tous les cours avec leurs projets ");
            System.out.println("7) visualiser tous les projets avec des informations supplémentaires ");
            System.out.println("8) visualiser toutes les compositions de groupe d’un projet");
            System.out.println("9) valider un groupe");
            System.out.println("10) valider tous les groupes d'un projet");
            value  = Integer.parseInt(scan.nextLine());
            try {
                if(value == 1) {
                    System.out.println("Entrez le code du cour (BINVxxxx) : ");
                    String code_cour = scan.nextLine();
                    System.out.println("Entrez le nom du cour : ");
                    String nom = scan.nextLine();
                    System.out.println("Entrez le bloc du cour : ");
                    String bloc = scan.nextLine();
                    System.out.println("Entrez le nombres de crédits du cour : ");
                    int nbrCredits = Integer.parseInt(scan.nextLine());
                    applicationEtudiant.ajouterUnCours( code_cour,nom,bloc,nbrCredits);
                }
                if(value == 2){
                    System.out.println("Entrez le nom de l'étudiant : ");
                    String nom = scan.nextLine();
                    System.out.println("Entrez le prénom de l'étudiant : ");
                    String prenom = scan.nextLine();
                    System.out.println("Entrez le mail de l'étudiant (...@student.vinci.be) : ");
                    String mail = scan.nextLine();
                    System.out.println("Entrez le mot de passe de l'étudiant : ");
                    String mdp = scan.nextLine();
                    mdp = BCrypt.hashpw(mdp, salt);
                    applicationEtudiant.ajouterUnEtudiant(prenom, nom, mail, mdp);
                }
                if(value == 3){
                    System.out.println("Entrez le mail de l'étudiant : ");
                    String mail = scan.nextLine();
                    System.out.println("Entrez l'identifiant unique du cour : ");
                    String identifiant_cours = scan.nextLine();
                    applicationEtudiant.ajouterEtudiantAUnCours(mail, identifiant_cours);
                }
                if(value == 4){
                    System.out.println("Entrez le nom du nouveau projet : ");
                    String nom = scan.nextLine();

                    System.out.println("Entrer la date de début du projet (JJ/MM/AAAA) : ");
                    String date_debut = scan.nextLine();

                    Date date_debut_Date = new SimpleDateFormat("dd/MM/yyyy").parse(date_debut);
                    System.out.println("Entrer la date de fin du projet (JJ/MM/AAAA) : ");
                    String date_fin = scan.nextLine();
                    Date date_fin_Date = new SimpleDateFormat("dd/MM/yyyy").parse(date_fin);

                    System.out.println("Entrez l'identifiant du nouveau projet : ");
                    String identifiant_projet = scan.nextLine();

                    System.out.println("Entrez le code cour du cours : ");
                    String code_cour = scan.nextLine();

                    int id_cour = 0;
                    PreparedStatement id_cour_Prepared = conn.prepareStatement("SELECT id_cour FROM projet.cours WHERE code_cour = ?");
                    try {
                        id_cour_Prepared.setString(1,code_cour);
                        ResultSet resultSet = id_cour_Prepared.executeQuery();
                        if (resultSet.next()) {
                            id_cour = resultSet.getInt("id_cour");
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    applicationEtudiant.ajouterUnProjetAUnCours(nom, date_debut_Date, date_fin_Date, identifiant_projet , id_cour);

                }
                if(value == 5){
                    System.out.println("Entrez l'identifiant du projet : ");
                    String id_projet = scan.nextLine();
                    System.out.println("Entrez le nombre de groupes à insérer : ");
                    int nbr_groupes = Integer.parseInt(scan.nextLine());
                    System.out.println("Entrez le nombre de places par groupes : ");
                    int nbr_places = Integer.parseInt(scan.nextLine());
                    applicationEtudiant.ajouterDesGroupesAUnProjet(id_projet,nbr_groupes,nbr_places);
                }
                if(value == 6){
                    applicationEtudiant.visualiserTousLesCours();
                }
                if(value == 7){
                    applicationEtudiant.visualiserTousLesProjets();
                }
                if(value == 8){
                    System.out.println("Entrez l'identifiant du projet : ");
                    String identifiant_projet = scan.nextLine();
                    int id_projet = 0;
                    PreparedStatement id_cour_Prepared = conn.prepareStatement("SELECT id_projet FROM projet.projets WHERE identifiant = ?");
                    try {
                        id_cour_Prepared.setString(1,identifiant_projet);
                        ResultSet resultSet = id_cour_Prepared.executeQuery();
                        if (resultSet.next()) {
                            id_projet = resultSet.getInt("id_projet");
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    applicationEtudiant.visualiserTousLesCompositionsDunProjet(id_projet);
                }
                if(value == 9){
                    System.out.println("Entrez l'identifiant du projet à valider : ");
                    String id = scan.nextLine();
                    System.out.println("Entrez le numero de groupe");
                    int num = Integer.parseInt(scan.nextLine());
                    applicationEtudiant.validerUnGroupe(id, num);
                }
                if(value == 10){
                    System.out.println("Entrez l'identifiant du projet à valider : ");
                    String id = scan.nextLine();
                    applicationEtudiant.validerTousLesGroupes(id);
                }
            }catch (Exception e) {
                System.out.println("Erreur avec les requêtes SQL !");
                System.exit(1);
            }
        }while (value >= 1 && value <= 10);
        applicationEtudiant.close();
    }
}