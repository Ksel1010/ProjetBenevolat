import java.sql.SQLException;
import java.util.Scanner;

public class Benevole extends User {

	public Benevole(String nom, String prenom, String ville, String mail, long n_tel) {
		super(nom, prenom, ville, mail, n_tel);

	}

	private String useType() {
		Scanner myObj = new Scanner(System.in);
		String option = myObj.nextLine();
		while (!(option.equals("-t") || option.equals("-d"))) {
			System.out.println("Rentrez -t pour voir les tâches ou -d pour te déconnecter");
			option = myObj.nextLine();
		}
		myObj.close();
		return option;
	}

	@Override
	public void utilisation() throws SQLException {
		System.out.println("Voir tes tâches -t ou deconnexion -d ");
		switch (useType()) {
		case "-t":

		break;

		case "-d":
			Main.iden.signOut();
		break;
		}

	}
}
