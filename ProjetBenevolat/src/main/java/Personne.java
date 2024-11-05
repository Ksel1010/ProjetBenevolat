import java.sql.SQLException;
import java.util.Scanner;

public class Personne extends User{
	static int nb = 0 ;
	public Personne(String nom, String prenom, String ville, String mail, long n_tel) {
		super(nom, prenom, ville, mail, n_tel);
		Personne.nb ++;
	}
	public static void setNb(int nb) {
		Personne.nb = nb;
	}
	public static int getNb() {
		return nb;
	}
	
	private String useType() {
		Scanner myObj = new Scanner(System.in);
		String option = myObj.nextLine();
		while (!(option.equals("-t") || option.equals("-a") || option.equals("-d"))) {
			System.out.println("Rentrez -t pour voir les tâches ou -a pour rajouter une tâche ou -d pour te déconnecter");
			option = myObj.nextLine();
		}
		myObj.close();
		return option;
	}
	
	@Override
	public void utilisation() throws SQLException {
		System.out.println("Voir tes tâches -t ou ajouter une tâche -a ou deconnexion -d ");
		switch (useType()) {
		case "-a":
			
			break;
			
		case "-t":
			
			break;

		case "-d":
			Main.iden.signOut();
			break;
		}
	}
}
