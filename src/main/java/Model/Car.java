package Model;

import java.util.ArrayList;
import java.util.List;

public class Car {
    private String brand;
    private String model;
    private int year;
    private int price;
    private Person owner = null;
    private FuelType fuelType;
    private String VIN;
    private Color color;

    private List<String> images = new ArrayList<>();

    public Car() {}
    private static Integer staticId = 0;
    private final Integer id = ++staticId;
    public Integer getId() { return id; }
    public Person getOwner() {
        return owner;
    }
    public void setOwner(Person owner) {
        this.owner = owner;
    }
    public Car(String brand, String model, int year, int price) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public String getVIN() {
        return VIN;
    }

    public Color getColor() {
        return color;
    }
    public void addImage(String image) { images.add(image); }
}

