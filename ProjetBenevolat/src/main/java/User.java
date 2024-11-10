import java.sql.SQLException;

public  class User {
	
	protected String nom;
	protected String prenom;
	protected String ville; 
	protected String mail;
	protected long n_tel;
	
	public User(String nom, String prenom, String ville, String mail, long n_tel) {
		this.nom = nom;
		this.prenom = prenom;
		this.ville = ville;
		this.mail = mail;
		this.n_tel = n_tel;
	}



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
}
