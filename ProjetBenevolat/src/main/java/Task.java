import java.sql.Date;

public class Task {
    private String status;
    private String description;
    private Personne demandeur;
    private Benevole benevole;
    private String title;
    private Date dateExpiration;


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

    public Task(String description, Personne demandeur, String titre) {
        this.description = description;
        this.demandeur = demandeur;
        this.title = titre;
        status = null;
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
}
