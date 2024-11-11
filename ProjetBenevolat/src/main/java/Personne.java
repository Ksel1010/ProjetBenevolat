import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Personne extends User{
	static int nb = 0 ;
	private final ArrayList<String> availableOptions = new ArrayList<>(List.of("-t", "-a", "-e", "-d"));
	private Scanner myObj ;
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
		myObj = new Scanner(System.in);
		String option = "";
		while (!availableOptions.contains(option)) {
			System.out.println("Rentrez -t pour voir les tâches ou -a pour rajouter une tâche ou -e pour effacer une tâche ou -d pour te déconnecter");
			option = myObj.nextLine();
		}
		return option;
	}
	
	@Override
	public void utilisation() throws SQLException {

		switch (useType()) {
		/*ajouter une tache*/
		case "-a":
			System.out.println("Rentrez le titre de votre tâche");
			String title = myObj.nextLine();
			System.out.println("Rentrez la description de la tâche");
			String description = myObj.nextLine();
			System.out.println("Rentrez yyyy-[m]m-[d]d");
			Date date = Date.valueOf(myObj.nextLine());
			Task task = new Task(this, title, description,date) ;
			SQLRequest.insert("Tasks", task);
			break;

		case "-t":
	/*voir mes tâches*/
			ArrayList<String> tables = new ArrayList<>(Arrays.asList("Tasks", "User"));
			ArrayList<String> conditions = new ArrayList<>(List.of("Tasks.mailInitializer = User.mail"));
			ArrayList<String> colonnes = new ArrayList<>(List.of("mail" ));
			ArrayList<String> values = new ArrayList<>(List.of(this.mail));
			ResultSet rs = SQLRequest.selectJoin(tables,conditions,colonnes,values);
			this.setTasks(rs);
			this.displayTasks();
			break;
	/*effacer une tâche */
		case "-e":
			tables = new ArrayList<>(Arrays.asList("Tasks", "User"));
			conditions = new ArrayList<>(List.of("Tasks.mailInitializer = User.mail"));
			colonnes = new ArrayList<>(List.of("mail" ));
			values = new ArrayList<>(List.of(this.mail));
			rs = SQLRequest.selectJoin(tables,conditions,colonnes,values);
			this.setTasks(rs);
			this.displayTasks();
			System.out.println("Saississez le numéro de la tâche à effacer : ");
			int i = Integer.valueOf(myObj.nextLine());
			if(i<=this.tasks.size() && i>0){
				SQLRequest.delete("Tasks", "Tasks.id", String.valueOf(this.tasks.get(i-1).getId()));
			}
			else {
				System.out.println("Choisissez une tâche à partir de la liste fournie");
			}
			break;
		/*deconnexion*/
		case "-d":
			Main.iden.signOut();
			break;
		}
		utilisation();
	}
}
