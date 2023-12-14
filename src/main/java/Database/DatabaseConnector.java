package Database;
import Model.Person;
import Util.Utils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {

    private  final String url = Utils.envConfig.getURL();
    private  final String user = Utils.envConfig.getUser();
    private   final String password =  Utils.envConfig.getPassword();
    private final Connection connection;

    public DatabaseConnector() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }

    public List<Person> getAllPersons() {
        final String SQL = "SELECT * FROM persons";
        try (
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            rs.next();
            String name = rs.getNString(1);
            System.out.println("name is : " + name);
            return new ArrayList<>();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }
}
