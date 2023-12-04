package Repository;

import Model.Car;
import Model.Person;

import java.util.ArrayList;
import java.util.List;

public class CarRepository {
    private final List<Car> cars;

    public CarRepository() {
        cars = new ArrayList<>();
    }

    public List<Car> getCars() { return cars; }

    public void addCar(Car car, Person owner) {
        cars.add(car);
        owner.addCar(car);
    }

    public void deleteCarById(int id) {
        cars.removeIf(car -> car.getId() == id);
    }
}
