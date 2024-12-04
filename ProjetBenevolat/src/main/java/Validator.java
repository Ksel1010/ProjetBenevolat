import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Validator {
    ArrayList<Task> taches = new ArrayList<>();
    String mail;
    public Validator(String mail) {
        this.mail = mail;
    }

    static private ArrayList<String> options = new ArrayList<>(List.of("-a","-v", "-t", "-s", "-r", "-d"));
    private Scanner myObj = new Scanner(System.in);
    public void utilisation() throws SQLException {
        switch (useType()){
            case "-a":
                /*ajouter un utilisateur a la liste*/
                ArrayList<String> validator = new ArrayList<>();
                ResultSet rs = SQLRequest.selectIsNull("User", "validator");
                int n = 1;
                System.out.printf("%-10s | %-15s | %-15s | %-50s\n", "numero", "nom","prenom", "e-mail");
                ArrayList<String> mails = new ArrayList<>();
                while(rs.next()){
                    System.out.printf("%-10d | %-15s | %-15s | %-50s\n",n, rs.getString("nom"), rs.getString("prenom"), rs.getString("mail"));
                    n++;
                    mails.add(rs.getString("mail"));
                }
                int i = n+1;
                while (i>n || i<0){
                    System.out.println("Saisissez le numero de l'utilisateur à rajouter: (0 pour annuler)");
                    i = myObj.nextInt();
                    myObj.nextLine(); // pour vider le buffer du caractere de saut de ligne
                }
                if(i==0)break;
                SQLRequest.update("User", "validator", this.mail, "mail = \'"+mails.get(i-1)+"\'");
                break;

            case "-t":
                /*afficher les taches a valider*/
                ArrayList<String> tables = new ArrayList<>(Arrays.asList("Tasks", "User"));
                ArrayList<String> conditions = new ArrayList<>(List.of("Tasks.mailInitializer = User.mail"));
                ArrayList<String> colonnes = new ArrayList<>(List.of("status" ));
                ArrayList<String> values = new ArrayList<>(List.of("a_valider"));
                rs = SQLRequest.selectJoin(tables,conditions,colonnes,values);
                this.setTasks(rs);
                this.displayTasks();
                break;

            case "-s":
                /*supprimer un utilisateur de la liste*/
                rs = SQLRequest.select("User", new ArrayList<>(List.of("validator")), new ArrayList<>(List.of(this.mail)));
                n = 1;
                System.out.printf("%-10s | %-15s | %-15s | %-50s\n", "numero", "nom","prenom", "e-mail");
                mails = new ArrayList<>();
                while(rs.next()){
                    System.out.printf("%-10d | %-15s | %-15s | %-50s\n",n, rs.getString("nom"), rs.getString("prenom"), rs.getString("mail"));
                    n++;
                    mails.add(rs.getString("mail"));
                }
                i = n+1;
                while (i>n || i<0){
                    System.out.println("Saisissez le numero de l'utilisateur à supprimer de la liste: (0 pour annuler)");
                    i = myObj.nextInt();
                    myObj.nextLine(); // pour vider le buffer du caractere de saut de ligne
                }
                if (i==0) break;
                SQLRequest.update("User", "validator", null, "mail = \'"+mails.get(i-1)+"\'");

                break;

            case "-v":
                /*choisir une tache a valider*/
                tables = new ArrayList<>(Arrays.asList("Tasks", "User"));
                conditions = new ArrayList<>(List.of("Tasks.mailInitializer = User.mail"));
                colonnes = new ArrayList<>(List.of("status" ));
                values = new ArrayList<>(List.of("a_valider"));
                rs = SQLRequest.selectJoin(tables,conditions,colonnes,values);
                this.setTasks(rs);
                if (this.taches.isEmpty()) break;
                this.displayTasks();
                do {
                    System.out.println("Choisir le numero de la tache à rendre valide ou 0 pour annuler");
                    i = myObj.nextInt();
                    myObj.nextLine(); // pour vider le buffer du caractere de saut de ligne
                }while (i>taches.size() && i>=0);
                if (i==0) break;
                SQLRequest.update("Tasks", "status", "valide", "id = "+taches.get(i-1).getId());
                break;
            case "-r":
                /*choisir une tache a valider*/
                rs = SQLRequest.select("Tasks", new ArrayList<String>(List.of("status")), new ArrayList<String>(List.of("a_valider"))) ;
                this.setTasks(rs);
                if (this.taches.isEmpty()) break;
                this.displayTasks();
                do {
                    System.out.println("Choisir le numero de la tache à refuser ou 0 pour annuler ");
                    i = myObj.nextInt();
                    myObj.nextLine(); // pour vider le buffer du caractere de saut de ligne
                }while (i>taches.size() && i>=0);
                if (i==0) break;
                SQLRequest.update("Tasks", "status", "refusee", "id = "+taches.get(i-1).getId());
                break;
            case "-d":
                /*deconnexion*/
                Main.iden.signOut();
                break;
        }
        this.utilisation();
    }

    private String useType() {
        String option = "";
        while (!options.contains(option)) {
            System.out.println("Pour rajouter un utilisateur à la liste taper -a, pour voir les tâches à valider taper -t pour valider une tache taper -v\npour refuser une tache -r pour enlever un utilisateur taper -s pour se déconnecter taper -d\n");
            option = myObj.nextLine();
        }
        return option;
    }

    void displayTasks() throws SQLException {
        System.out.printf("%-10s | %-20s | %-15s | %-15s | %-10s | %-20s | %-20s\n", "Numero","Titre","DateExpiration","Ville","Status","Demandeur", "Bénévole");
        for (int i = 0 ; i<this.taches.size();i++){
            System.out.printf("%-10d | ",i+1);
            taches.get(i).displayTask();
        }
    }
    public void setTasks(ResultSet rs) throws SQLException {
        this.taches = new ArrayList<>();
        while (rs.next()){
            this.taches.add(new Task(rs));
        }

    }
    public ArrayList<Task> getTasks(){
        return  this.taches;
    }
}
