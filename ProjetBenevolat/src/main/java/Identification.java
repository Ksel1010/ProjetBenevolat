
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Identification {
	private Connection connection;
	private Scanner myObj ;
	private User user;


	public Identification()  {

	}

	
	public void start() throws SQLException {
		System.out.println("Voulez vous vous connecter -signIn ou créer un nouveau profil -signUp ");
		myObj = new Scanner(System.in);
		String page = myObj.nextLine();
		while (!(page.equals("-signIn") || page.equals("-signUp"))) {
			System.out.println("Pour se connecter taper \"-signIn\" et pour s'inscrire taper \"-signUp\" ");
			page = myObj.nextLine();
		}
		if (page.equals("-signIn")) {
			try {
				this.signIn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else
			this.signUp();
	}

	
	private String connectType() {
		System.out.println(
				"Voulez vous vous connecter en tant que bénévole -b ou en tant que personne en besoin d'aide -a pour les validateurs taper -v");
		String option = myObj.nextLine();
		while (!(option.equals("-a") || option.equals("-b") || option.equals("-v"))) {
			System.out.println("Rentrez -a pour personne en besoin d'aide ou -b pour personne bénévole ou -v pour validateur");
			option = myObj.nextLine();
		}
		return option;
	}
	
	public void signIn() throws SQLException {
		String option = connectType();
		myObj = new Scanner(System.in);
		System.out.println("Donnez votre mail");
		String mail = myObj.nextLine();
		System.out.println("Donnez votre mot de passe");
		String mdp = myObj.nextLine();
		if(option.equals("-v")){
			ResultSet rs= SQLRequest.select("Validators", new ArrayList<>(List.of("mail", "mdp")), new ArrayList<>(List.of(mail, mdp)));
			if(rs.next()){
				Validator v = new Validator(mail);
				v.utilisation();
			}
			else {
				System.out.println("Erreur d'authentification");
				this.signIn();
			}
		}
		else {
			ArrayList<String> tables = new ArrayList<>(List.of("AccessControl", "User"));
			ArrayList<String> conditions = new ArrayList<>(List.of("AccessControl.mail = User.mail"));
			ArrayList<String> colonnes = new ArrayList<>(List.of("AccessControl.mail", "mdp"));
			ArrayList<String> values = new ArrayList<>(List.of(mail, mdp));

			ResultSet rs = SQLRequest.selectJoin(tables, conditions, colonnes, values);

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
				rs = SQLRequest.select("User", new ArrayList<>(List.of("mail")), new ArrayList<>(List.of(mail)));
				if (rs.next()) {
					System.out.println("Mot de passe erronné");
					this.signUp();
				} else {
					System.out.println("Adresse mail inconnue");
					this.start();
				}
			}
		}
	}
	
	public void signUp() throws SQLException {
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

		user = new User(nom, prenom, ville, mail, tel);

		SQLRequest.insert("User", user);
		SQLRequest.insert("AccessControl", new String[]{mail, mdp}) ;

		String option = connectType();
		if (option.equals("-a")) {
			user = new Personne(user.getNom(), user.getPrenom(),user.getVille(),user.getMail(),user.getN_tel());
		} else {
			user = new Benevole(user.getNom(), user.getPrenom(),user.getVille(),user.getMail(),user.getN_tel());
		}

		System.out.println("Bienvenue " + user.toString());
		user.utilisation();
		System.out.println("Bienvenue " + prenom);

	}

	public void signOut() throws SQLException {
		SQLRequest.exit();
		myObj.close();
		System.out.println("Vous avez été déconnecté");
		System.exit(0);
	}
}
