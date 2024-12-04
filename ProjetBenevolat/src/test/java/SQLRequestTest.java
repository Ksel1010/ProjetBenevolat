import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;



import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLRequestTest {
    static ArrayList<Personne> personnes = new ArrayList<>();
    static ArrayList<Task> tasks = new ArrayList<>();
    static ArrayList<Pair> accessControl = new ArrayList();
    static int nb_personnes = 5;
    static int nb_tasks = 10;
    public record Pair(String mail, String pwd){};

    @BeforeAll
    public static void setUp(){
        for(int i =0; i<nb_personnes; i++){
            personnes.add(new Personne("USER"+i, "user_"+i, "ville"+i, "user"+i+"@mail.fr", 0612345600+i));
            accessControl.add(new Pair("user"+i+"@mail.fr", "toto"+i));
        }
        for(int i=0;i<nb_tasks;i++){
            tasks.add(new Task(personnes.get(i%personnes.size()), "task"+i, "description", Date.valueOf(("2025-12-1"))));
        }
    }
    @Test
    @Order(1)
    public void testInsertUser(){
        int table_size = getTableSize("User");
        for (Personne personne : personnes) {
            try {
                SQLRequest.insert("User", personne);
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        assertEquals(table_size+nb_personnes, getTableSize("User"));
    }

    @Test
    @Order(2)
    public void testInsertAccessControl(){
        int table_size = getTableSize("User");
        for (Pair pair : accessControl) {
            try {
                SQLRequest.insert("AccessControl", new String[]{pair.mail, pair.pwd});
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        assertEquals(table_size, getTableSize("AccessControl"));
    }

    @Test
    @Order(3)
    public void testInsertTasks(){
        int table_size = getTableSize("Tasks");
        for (Task t : tasks) {
            try {
                SQLRequest.insert("Tasks", t);
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        assertEquals(table_size+nb_tasks, getTableSize("Tasks"));

    }
    public int getTableSize(String table) {
        int table_size = 0;

        try {
            ResultSet rs = SQLRequest.select(table, new ArrayList<>(), new ArrayList<>());
            while (rs.next()){
                table_size++;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return table_size;
    }

    @Test
    @Order(4)
    public void testSelect(){
        ResultSet rs ;
        for (Personne p : personnes){
            try {
                rs = SQLRequest.select("User",new ArrayList<>(List.of("nom", "prenom")), new ArrayList<>(List.of(p.nom, p.prenom)));
                rs.next();
                assertEquals(p.nom, rs.getString("nom"));
                assertEquals(p.prenom, rs.getString("prenom"));
                assertEquals(p.ville, rs.getString("ville"));
                assertEquals(p.mail, rs.getString("mail"));
                assertEquals(p.n_tel, rs.getInt("tel"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    @Order(5)
    public void testSelectIsNull(){

}
    @Test
    @Order(6)
    public void testSelectJoin(){

    }

    @Test
    @Order(7)
    public void testUpdate(){

    }

    @Test
    @Order(8)
    public void testDelete(){
        int size_table = getTableSize("User");
        assertEquals(size_table, getTableSize("AccessControl"));
        for(Personne p:personnes){
            try {
                SQLRequest.delete("User", "mail", p.mail);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        assertEquals(size_table-nb_personnes, getTableSize("User"));
        assertEquals(size_table-nb_personnes, getTableSize("AccessControl"));

    }


}
