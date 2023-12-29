package Database;
import Model.Person;
import Util.Utils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {

    private final String url = Utils.envConfig.getURL();
    private final String user = Utils.envConfig.getUser();
    private final String password =  Utils.envConfig.getPassword();
    private final Connection connection;

    public DatabaseConnector() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        String createPersonsTable = "CREATE TABLE IF NOT EXISTS Person (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    username TEXT,\n" +
                "    email TEXT,\n" +
                "    password TEXT\n" +
                ");";
        executeQuerySQL(createPersonsTable);
        String createCarsTable = "CREATE TABLE IF NOT EXISTS Car (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    brand TEXT,\n" +
                "    model TEXT,\n" +
                "    year INTEGER,\n" +
                "    price INTEGER,\n" +
                "    owner_id INTEGER,\n" +
                "    fuelType TEXT,\n" +
                "    VIN TEXT,\n" +
                "    color TEXT,\n" +
                "    FOREIGN KEY (owner_id) REFERENCES Person(id)\n" +
                ");\n";
        executeQuerySQL(createCarsTable);
    }

    private void executeQuerySQL(String query) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<Person> getAllPersons() {
        final String SQL = "SELECT * FROM Person";
        try (
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            List<Person> persons = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt(1);
                String username = rs.getString(2);
                String email = rs.getString(3);
                String password = rs.getString(4);
                persons.add(new Person(id, username, email, password));
            }
            return persons;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }
}
