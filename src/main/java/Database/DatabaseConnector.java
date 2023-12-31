package Database;
import Model.Car;
import Model.Color;
import Model.FuelType;
import Model.Person;
import Util.Utils;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
                "    FOREIGN KEY (owner_id) REFERENCES Person(id) ON DELETE CASCADE \n" +
                ");\n";
        executeQuerySQL(createCarsTable);
        String createCarImagesTable = "CREATE TABLE IF NOT EXISTS Image (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    url TEXT,\n" +
                "    carId INTEGER,\n" +
                "    FOREIGN KEY (carId) REFERENCES Car(id) ON DELETE CASCADE\n" +
                ");";
        executeQuerySQL(createCarImagesTable);
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

    public Optional<Person> getUserById(int id) {
        final String SQL = "SELECT * FROM Person WHERE id="+id;
        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {

            Optional<Person> person = Optional.empty();

            if (rs.next()) {
                String username = rs.getString(2);
                String email = rs.getString(3);
                String password = rs.getString(4);
                person = Optional.of(new Person(id, username, email, password));
            }
            return person;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return Optional.empty();
        }
    }

    public boolean deleteUserById(int id) {
        final String SQL = "DELETE FROM Person WHERE id="+id;
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(SQL);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public Person registerUser(Person person) {
        final String SQL = "INSERT INTO Person (username, email, password) VALUES (?, ?, ?)";
        try (
                PreparedStatement stmt = connection.prepareStatement(SQL)
                ){
            stmt.setString(1, person.getUsername());
            stmt.setString(2, person.getEmail());
            stmt.setString(3, person.getPassword());

            stmt.executeUpdate();

            return person;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public void updateUser(Person person) {
        final String SQL = "UPDATE Person SET username = ?, email = ?, password = ? WHERE id = ?;";
        try (
                PreparedStatement stmt = connection.prepareStatement(SQL)
        ){
            stmt.setString(1, person.getUsername());
            stmt.setString(2, person.getEmail());
            stmt.setString(3, person.getPassword());
            stmt.setString(4, person.getId().toString());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void addCarImage(String url, int id) {
        final String SQL = "INSERT INTO Image (url, carId)\n" +
                "VALUES (?, ?);";
        try (
                PreparedStatement stmt = connection.prepareStatement(SQL)
                ) {
            stmt.setString(1, url);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Optional<Car> getCarById(int id) {
        final String SQL = "SELECT * FROM Car WHERE id="+id;
        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(SQL);
                ) {
            if (rs.next() )
                return Optional.of(new Car(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getInt(6),//owner id
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9)));
            return Optional.empty();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public void addCar(Car car, int owner_id) {
        final String SQL = "INSERT INTO Car (brand, model, year, price, owner_id, fuelType, VIN, color)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try (
                PreparedStatement stmt = connection.prepareStatement(SQL);
                ) {
            stmt.setString(1, car.getBrand());
            stmt.setString(2, car.getModel());
            stmt.setInt(3, car.getYear());
            stmt.setInt(4, car.getPrice());
            stmt.setInt(5, owner_id);
            stmt.setString(6, car.getFuelType().toString());
            stmt.setString(7, car.getVIN());
            stmt.setString(8, car.getColor().toString());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Car> getCarsOfUser(int id) {
        final String SQL = "SELECT * FROM Car WHERE owner_id="+id;
        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(SQL);
                ) {
            List<Car> cars = new LinkedList<>();
            while (rs.next()) {
                int carId = rs.getInt("id");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                int year = rs.getInt("year");
                int price = rs.getInt("price");
                int ownerId = rs.getInt("owner_id");
                String fuelType = rs.getString("fuelType");
                String VIN = rs.getString("VIN");
                String color = rs.getString("color");
                Car car = new Car(carId, brand, model, year, price, ownerId, (fuelType), VIN, (color));
                cars.add(car);
            }
            return cars;
        } catch ( Exception ex) {
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }

    public void deleteCarById(int carId) {
        final String SQL = "DELETE FROM Car WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, carId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Car with ID " + carId + " deleted successfully.");
            } else {
                System.out.println("No car found with ID " + carId);
            }
        } catch (SQLException ex) {
            System.out.println("Error deleting car: " + ex.getMessage());
        }
    }
}
