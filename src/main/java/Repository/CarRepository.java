package Repository;

import Model.Car;
import Model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Optional<Car> findCarById(int id) { return cars.stream().filter(car -> car.getId() == id).findFirst(); }

    public void addImageToCar(String imageURL, int id) {
        cars.stream().filter(car -> car.getId()==id).forEach( car -> car.addImage(imageURL));
    }
}
