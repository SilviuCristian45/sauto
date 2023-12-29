package Model;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private String username;
    private String email;
    private String password;
    private final List<Car> cars = new ArrayList<>();

    private static Integer staticId = 0;
    private Integer id = ++staticId;

    public Person(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Integer getId() { return id; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "( " + this.getId() + " \n email : " + email + " \n username: " + username + "\n password: " + password + "\n )";
    }

    public void addCar(Car car) {
        if (!cars.contains(car)) {
            car.setOwner(this);
            cars.add(car);
        }
    }

    public List<Car> getCars() {
        return cars;
    }
}

