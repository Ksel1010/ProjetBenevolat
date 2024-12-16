import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Task {
    private String status;
    private String description;
    private Personne demandeur;
    private Benevole benevole;
    private String title;
    private Date dateExpiration;

    private int id;


    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Benevole getBenevole() {
        return benevole;
    }

    public void setBenevole(Benevole benevole) {
        this.benevole = benevole;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Personne getDemandeur() {
        return demandeur;
    }

    public Benevole getbenevole() {
        return benevole;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDemandeur(Personne demandeur) {
        this.demandeur = demandeur;
    }

    public void setbenevole(Benevole benevole) {
        this.benevole = benevole;
    }

    public void setTitle(String titre) {
        this.title = titre;
    }

    public Task(Personne demandeur, String titre, String description, Date date) {
        this.description = description;
        this.demandeur = demandeur;
        this.title = titre;
        this.dateExpiration = date;
        status = "valide";
        benevole = null;
    }
    public Task(Personne demandeur, String titre, String description, Date date, String status) {
        this.description = description;
        this.demandeur = demandeur;
        this.title = titre;
        this.dateExpiration = date;
        this.status = status;
        benevole = null;
    }

    public Task(ResultSet rs) {


        try {
            this.demandeur = new Personne(rs.getString("nom"),rs.getString("prenom"),
                    rs.getString("ville"), rs.getString("mail"), rs.getInt("tel"));
            this.title = rs.getString("title");
            this.description = rs.getString("description");
            this.dateExpiration = rs.getDate("dateExpiration");
            this.status = rs.getString("status");
            this.id = rs.getInt("id");
            String mailBenevole = rs.getString("mailBenevole");
            if (mailBenevole!=null) this.benevole = new Benevole(mailBenevole);
            else this.benevole = null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    public String getMailInitializer() {
        return demandeur.getMail();
    }

    public String getMailBenevole(){
        return  benevole.getMail();
    }

    public Date getDateExpiration() {
        return dateExpiration;
    }


    public void displayTask() {
        System.out.printf("%-25s | %-15s | %-15s | %-10s | %-20s | %-20s\n", this.title,this.dateExpiration.toString(),
                this.demandeur.getVille(), this.status,this.demandeur.toString(), ( (this.benevole != null) ? this.benevole.toString() : "-"));
    }

    public void displayDescription(){
        System.out.println("Description : "+this.description);
    }

    public void displayPhoneAsker(){
        System.out.println("Téléphone du démandeur :"+this.demandeur.getN_tel());
    }

    public void displayPhoneBenevole(){
        System.out.println("Téléphone du bénevole :"+( (this.benevole != null) ? this.benevole.getN_tel() : "-"));
    }
}
