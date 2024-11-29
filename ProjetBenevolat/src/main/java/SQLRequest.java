import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLRequest {

    private static Connection connection;
    private static String sql;
    private static PreparedStatement preparedStatement;
    private static final ArrayList<String>listOfExistingTables = new ArrayList<>(Arrays.asList("Tasks","AccessControl","User", "Validators"));

    static {

        try {
            String url = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_022";
            String username = "projet_gei_022";
            String mdp = "Deph4ohj";
            SQLRequest.connection = DriverManager.getConnection(url, username, mdp);
        } catch (SQLException e) {

        }
    }

    public  static void insert(String table, Object o)throws SQLException{
        switch (table){

            case "User":
                User user = (User)o;
                sql = "INSERT INTO User(nom, prenom, mail, ville, tel) VALUES (?,?,?,?,?)";
                preparedStatement = SQLRequest.connection.prepareStatement(sql);
                preparedStatement.setString(1, user.getNom());
                preparedStatement.setString(2, user.getPrenom());
                preparedStatement.setString(3, user.getMail());
                preparedStatement.setString(4, user.getVille());
                preparedStatement.setString(5, String.valueOf(user.getN_tel()));
                preparedStatement.executeUpdate();
                break;
            case "Tasks":
                Task task = (Task) o;
                sql = "INSERT INTO Tasks(mailInitializer, description, title, dateExpiration,status) VALUES (?,?,?,?,?)";
                preparedStatement = SQLRequest.connection.prepareStatement(sql);
                preparedStatement.setString(1, task.getMailInitializer());
                preparedStatement.setString(2, task.getDescription());
                preparedStatement.setString(3, task.getTitle());
                preparedStatement.setDate(4, task.getDateExpiration());
                preparedStatement.setString(5, task.getStatus());
                preparedStatement.executeUpdate();

                break;
            case "AccessControl":
                String[] li = (String[])o;
                sql = "INSERT INTO AccessControl(mail, mdp) VALUES (?,?)";
                preparedStatement = SQLRequest.connection.prepareStatement(sql);
                preparedStatement.setString(1, li[0]);
                preparedStatement.setString(2, li[1]);
                preparedStatement.executeUpdate();
                break;
        }
    }

        public static ResultSet select(String table, ArrayList<String> colonnes, ArrayList<String> values) throws SQLException {
            if (listOfExistingTables.contains(table)){
                return selectExistingTable(table, colonnes, values);
            }
            else throw new SQLException("No table matches the select request") ;

        }

        public static ResultSet selectIsNull(String table, String colonne) throws SQLException {
            if (listOfExistingTables.contains(table)){
                sql = "select * from "+table+" where "+colonne+" is null";
                preparedStatement = SQLRequest.connection.prepareStatement(sql);
                return preparedStatement.executeQuery();
            }else throw new SQLException("Table non existante");
        }

        private static ResultSet selectExistingTable(String table, ArrayList<String> colonnes, ArrayList<String> values){
            sql = "Select * from "+table+" where ";
            for (int i= 0; i<colonnes.size()-1;i++){

                sql+=(colonnes.get(i)+"= ?"+" and ");
            }
            sql+= (colonnes.get(colonnes.size()-1)+" = ?");
            try {
                preparedStatement = SQLRequest.connection.prepareStatement(sql);
                for(int i= 0; i<values.size();i++){
                    preparedStatement.setString(i+1, values.get(i));
                }
                return preparedStatement.executeQuery();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public static ResultSet selectJoin(ArrayList<String> tables, ArrayList<String>joinConditions, ArrayList<String>colonnes, ArrayList<String>values) throws SQLException {
            String tablesJoin;
            if (listOfExistingTables.contains(tables.get(0))) tablesJoin = tables.get(0);
            else throw new SQLException("Tables non existante : "+tables.get(0));
            for (int i=0 ;i<joinConditions.size();i++){
                if(listOfExistingTables.contains(tables.get(i+1)))
                tablesJoin+=" inner join "+ tables.get(i+1)+" on "+joinConditions.get(i);
                else throw new SQLException("Tables non existante : "+tables.get(i+1));
            }
            return selectExistingTable(tablesJoin, colonnes, values);
        }

    public static void update(String table, String colonne, String valeur, String condition) throws SQLException{
        if (listOfExistingTables.contains(table)){
            if (valeur==null){
                sql = "UPDATE "+table+ " SET " + colonne + "= NULL WHERE "+condition;
                preparedStatement = SQLRequest.connection.prepareStatement(sql);
            }else {
                sql = "UPDATE " + table + " SET " + colonne + "= ? WHERE " + condition;
                preparedStatement = SQLRequest.connection.prepareStatement(sql);
                preparedStatement.setString(1, valeur);
            }
            preparedStatement.executeUpdate();
        }
    }

    public static void delete(String table, String colonne, String valeur) throws SQLException {
        if (listOfExistingTables.contains(table)){
            sql = "DELETE from "+ table + " where " + colonne + " = ?";
            preparedStatement = SQLRequest.connection.prepareStatement(sql);
            preparedStatement.setString(1, valeur);
            preparedStatement.executeUpdate();
        }
    }
    public static void exit() throws SQLException {
        SQLRequest.connection.close();
    }
}
