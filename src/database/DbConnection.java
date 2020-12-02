package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    public static Connection connect(){
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con =DriverManager.getConnection("jdbc:sqlite:TurismoTierraMedia.db");
        }catch (ClassNotFoundException | SQLException exception){
            System.out.println(exception+"");
        }
        return con;
    }
}
