package Util;

import Repository.CarRepository;
import Repository.PersonRepository;

public class Repositories {
    public static PersonRepository personRepository = new PersonRepository();
    public static CarRepository carRepository = new CarRepository();
}
