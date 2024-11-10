import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Benevole extends User {
	private Scanner myObj ;

	public Benevole(String nom, String prenom, String ville, String mail, long n_tel) {
		super(nom, prenom, ville, mail, n_tel);

	}

	private String useType() {
		myObj = new Scanner(System.in);
		String option = "";
		while (!(option.equals("-t") || option.equals("-d") || option.equals("-m"))) {
			System.out.println("Rentrez -t pour voir les tâches -m pour voir tes missions ou -d pour te déconnecter");
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
				sql = "select * from Tasks, User " + "where mailBenevole = ? and  User.mail = Tasks.mailInitializer ";
				preparedStatement = Main.iden.getConnection().prepareStatement(sql);
				preparedStatement.setString(1, this.mail);
				rs = preparedStatement.executeQuery();
				System.out.print("Titre \t\t\t\t description\t\t ville\t dateExpiration\t Status\t Demandeur");
				if (rs.next()) {
					System.out.println(rs.getString("title")+"\t"+rs.getString("description")+"\t"+rs.getString("ville")
							+"\t"+rs.getDate("dateExpiration")+"\t"+rs.getString("status")+"\t"+rs.getString("mailInitializer"));
				}
				break;
			case "-t":
				sql = "select * from Tasks, User " + "where mailInitializer = ? and  User.mail = Tasks.mailInitializer";
				preparedStatement = Main.iden.getConnection().prepareStatement(sql);
				preparedStatement.setString(1, this.mail);
				rs = preparedStatement.executeQuery();
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

	}
}
