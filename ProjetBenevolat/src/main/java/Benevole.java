import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Benevole extends User {
	private Scanner myObj ;
	private final ArrayList<String> availableOptions = new ArrayList<>(List.of("-t", "-m", "-s", "-d"));


	public Benevole(String nom, String prenom, String ville, String mail, long n_tel) {
		super(nom, prenom, ville, mail, n_tel);

	}

	public Benevole(String mail){

		try {
			ResultSet rs = SQLRequest.select("User", new ArrayList<>(List.of("mail")), new ArrayList<>(List.of(mail)));
			if (rs.next()){
			this.nom = rs.getString("nom");
			this.prenom = rs.getString("prenom");
			this.ville = rs.getString("ville");
			this.mail = mail;
			this.n_tel = rs.getInt("tel");}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private String useType() {
		myObj = new Scanner(System.in);
		String option = "";
		while (!availableOptions.contains(option)) {
			System.out.println("Rentrez -t pour voir les tâches -m pour voir tes missions ou -s pour selectionner une tâche ou -d pour te déconnecter");
			option = myObj.nextLine();
		}
		return option;
	}

	@Override
	public void utilisation() throws SQLException {
		String sql;
		PreparedStatement preparedStatement;
		ResultSet rs;
		switch (useType()) {
			case "-m":
				ArrayList<String> tables = new ArrayList<>(List.of("Tasks", "User"));
				ArrayList<String> conditions = new ArrayList<>(List.of("Tasks.mailInitializer = User.mail"));
				ArrayList<String> colonnes = new ArrayList<>(List.of("mailBenevole" ));
				ArrayList<String> values = new ArrayList<>(List.of(this.mail));
				rs = SQLRequest.selectJoin(tables,conditions,colonnes,values);
				this.setTasks(rs);
				this.displayTasks();
				break;
			case "-t":

				tables = new ArrayList<>(List.of("Tasks", "User"));
				conditions = new ArrayList<>(List.of("Tasks.mailInitializer = User.mail"));
				colonnes = new ArrayList<>(List.of("status"));
				values = new ArrayList<>(List.of("valide"));
				rs = SQLRequest.selectJoin(tables,conditions,colonnes,values);
				this.setTasks(rs);
				this.displayTasks();
				break;

			case "-s":

				tables = new ArrayList<>(List.of("Tasks", "User"));
				conditions = new ArrayList<>(List.of("Tasks.mailInitializer = User.mail"));
				colonnes = new ArrayList<>(List.of("status"));
				values = new ArrayList<>(List.of("valide"));
				rs = SQLRequest.selectJoin(tables,conditions,colonnes,values);
				this.setTasks(rs);
				this.displayTasks();
				System.out.println("Saississez le numéro de la tâche : ");
				int i = Integer.valueOf(myObj.nextLine());
				SQLRequest.update("Tasks", "status", "en_cours", "id = "+tasks.get(i-1).getId());
				SQLRequest.update("Tasks", "mailBenevole", this.mail, "id = "+tasks.get(i-1).getId());
				break;

			case "-d":
				Main.iden.signOut();
				break;
		}
		this.utilisation();

	}
}
