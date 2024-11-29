import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public  class User {
	
	protected String nom;
	protected String prenom;
	protected String ville; 
	protected String mail;
	protected long n_tel;

	ArrayList<Task> tasks = new ArrayList<>();
	
	public User(String nom, String prenom, String ville, String mail, long n_tel) {
		this.nom = nom;
		this.prenom = prenom;
		this.ville = ville;
		this.mail = mail;
		this.n_tel = n_tel;
	}

	public User(){}

	public String getPrenom() {
		return prenom;
	}

	public String getNom() {
		return nom;
	}

	public String getVille() {
		return ville;
	}

	public String getMail() {
		return mail;
	}

	public long getN_tel() {
		return n_tel;
	}
	
	public String toString() {
		return this.nom + " " + this.prenom;
	}
	
	public void utilisation() throws SQLException{};

	public void setTasks(ResultSet rs) {
		this.tasks = new ArrayList<>();
		try {
		while(rs.next()) {
			this.tasks.add(new Task(rs));
		} }catch (SQLException e) {
				throw new RuntimeException(e);
			}

		}


	public void displayTasks(){
		System.out.printf("%-10s | %-20s | %-15s | %-15s | %-10s | %-20s | %-20s\n", "Numero","Titre","DateExpiration","Ville","Status","Demandeur", "Bénévole");
		for (int i=0;i<this.tasks.size();i++){
			System.out.printf("%-10s | ",String.valueOf(i+1));
			tasks.get(i).displayTask();
		}
	}


}
