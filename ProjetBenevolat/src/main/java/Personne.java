import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Personne extends User{
	static int nb = 0 ;
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
		while (!(option.equals("-t") || option.equals("-a") || option.equals("-d"))) {
			System.out.println("Rentrez -t pour voir les tâches ou -a pour rajouter une tâche ou -d pour te déconnecter");
			option = myObj.nextLine();
		}
		return option;
	}
	
	@Override
	public void utilisation() throws SQLException {

		String sql;
		PreparedStatement preparedStatement;
		switch (useType()) {
		case "-a":
			System.out.println("Rentrez le titre de votre tâche");
			String title = myObj.nextLine();
			System.out.println("Rentrez la description de la tâche");
			String description = myObj.nextLine();
			System.out.println("Rentrez yyyy-[m]m-[d]d");
			Date date = Date.valueOf(myObj.nextLine());
			Task task ;
			SQLRequest.insert("Tasks", task);
			break;

		case "-t":
			sql = "select * from Tasks, User " + "where mailInitializer = ? and  User.mail = Tasks.mailInitializer";
			preparedStatement = Main.iden.getConnection().prepareStatement(sql);
			preparedStatement.setString(1, this.mail);
			ResultSet rs = preparedStatement.executeQuery();
			System.out.print("Titre \t\t\t\t description\t\t ville\t dateExpiration\t Status\t Bénévole");
			if (rs.next()) {
				System.out.println(rs.getString("title")+"\t"+rs.getString("description")+"\t"+rs.getString("ville")
						+"\t"+rs.getDate("dateExpiration")+"\t"+rs.getString("status")+"\t"+rs.getString("mailBenevole"));
			}
			break;

		case "-d":
			Main.iden.signOut();
			break;
		}
		utilisation();
	}
}
