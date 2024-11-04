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
	
	@Override
	public void utilisation() {
		System.out.println("Voir tes tâches -t ou ajouter une tâche -a ou deconnexion -d ");
		
	}
}
