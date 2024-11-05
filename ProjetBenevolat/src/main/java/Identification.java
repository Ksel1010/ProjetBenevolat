
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Identification {
	private Connection connection;
	private Scanner myObj = new Scanner(System.in);
	private User user;

	public Connection getConnection() {
		return connection;
	}

	public Identification() throws SQLException {
		String url = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_022";
		String username = "projet_gei_022";
		String mdp = "Deph4ohj";
		this.connection = DriverManager.getConnection(url, username, mdp);

	}

	
	public void start() throws SQLException {
		System.out.println("Voulez vous vous connecter -signIn ou créer un nouveau profil -signUp");
		myObj = new Scanner(System.in);
		String page = myObj.nextLine();
		while (!(page.equals("-signIn") || page.equals("-signUp"))) {
			System.out.println("Pour se connecter taper \"-signIn\" et pour s'inscrire taper \"-signUp\"");
			page = myObj.nextLine();
		}
		if (page.equals("-signIn")) {
			try {
				this.singIn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else
			this.signUp();
	}

	
	private String connectType() {
		System.out.println(
				"Voulez vous vous connecter en tant que bénévole -b ou en tant que personne en besoin d'aide -a");
		String option = myObj.nextLine();
		while (!(option.equals("-a") || option.equals("-b"))) {
			System.out.println("Rentrez -a pour personne en besoin d'aide ou -b pour personne bénévole");
			option = myObj.nextLine();
		}
		return option;
	}
	
	public void signUp() throws SQLException {
		String option = connectType();
		myObj = new Scanner(System.in);
		System.out.println("Donnez votre mail");
		String mail = myObj.nextLine();
		System.out.println("Donnez votre mot de passe");
		String mdp = myObj.nextLine();
		String sql = "select * from AccessControl, User " + "where mdp= ? and AccessControl.mail =?"
				+ "and AccessControl.mail = User.mail";
		PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
		preparedStatement.setString(1, mdp);
		preparedStatement.setString(2, mail);
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			if (option.equals("-a")) {
				user = new Personne(rs.getString("nom"), rs.getString("prenom"), rs.getString("ville"),
						rs.getString("mail"), rs.getInt("tel"));

			} else {
				user = new Benevole(rs.getString("nom"), rs.getString("prenom"), rs.getString("ville"),
						rs.getString("mail"), rs.getInt("tel"));

			}
			System.out.println("Bienvenue " + user.toString());
			user.utilisation();
			
		} else {
			System.out.println("Erreur d'authentification");
			sql = "select * from User where mail = ?";
			preparedStatement = this.connection.prepareStatement(sql);
			preparedStatement.setString(1, mail);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				System.out.println("Mot de passe erronné");
				this.signUp();
			} else {
				System.out.println("Adresse mail inconnue");
				this.start();
			}
		}

	}
	
	public void singIn() throws SQLException {
		myObj = new Scanner(System.in);

		System.out.println("Rentrez votre nom");
		String nom = myObj.nextLine();
		System.out.println("Rentrez votre prénom");
		String prenom = myObj.nextLine();
		System.out.println("Rentrez votre mail");
		String mail = myObj.nextLine();
		System.out.println("Rentrez votre mot de passe");
		String mdp = myObj.nextLine();
		System.out.println("Rentrez votre numéro de téléphone");
		long tel = Integer.parseInt(myObj.nextLine());
		System.out.println("Rentrez votre ville");
		String ville = myObj.nextLine();

		String sql = "INSERT INTO User(nom, prenom, mail, ville, tel) VALUES (?,?,?,?,?)";
		PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
		preparedStatement.setString(1, nom);
		preparedStatement.setString(2, prenom);
		preparedStatement.setString(3, mail);
		preparedStatement.setString(4, ville);
		preparedStatement.setString(5, String.valueOf(tel));

		int rowsInserted = preparedStatement.executeUpdate();
		sql = "INSERT INTO AccessControl(mail, mdp) VALUES (?,?)";
		preparedStatement = this.connection.prepareStatement(sql);
		preparedStatement.setString(1, mail);
		preparedStatement.setString(2, mdp);
		rowsInserted = preparedStatement.executeUpdate();

		String option = connectType();
		if (option.equals("-a")) {
			user = new Personne(nom, prenom, ville, mail, tel);
		} else {
			user = new Benevole(nom, prenom, ville, mail, tel);

		}
		System.out.println("Bienvenue " + user.toString());
		user.utilisation();
		System.out.println("Bienvenue " + prenom);

	}

	public void signOut() throws SQLException {
		connection.close();
		myObj.close();
		System.out.println("Vous avez été déconnecté");
		System.exit(0);
	}
}
