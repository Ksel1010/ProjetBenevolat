import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class Main {

	public static Identification iden = new Identification();
	public static void main(String[] args) throws SQLException {
		
		 
		SQLRequest.select("User", new ArrayList<>(List.of("nom")),new ArrayList<>(List.of("test")));
		 iden.start();
	}

}
