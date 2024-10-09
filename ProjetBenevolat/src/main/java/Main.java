import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) throws SQLException {
		String url = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_022";
		String username = "projet_gei_022";
		String mdp = "Deph4ohj";
		
		 Connection connection = DriverManager.getConnection(url, username, mdp);
		 
		 String sql = "INSERT INTO Benevole(id , nom, prenom) VALUES (0, 'value1s', 'value2')";
         PreparedStatement preparedStatement = connection.prepareStatement(sql);

      
         int rowsInserted = preparedStatement.executeUpdate();
         System.out.println(rowsInserted + " row(s) inserted.");
		 connection.close();
	}

}
