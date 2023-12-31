package Model;

import java.util.ArrayList;
import java.util.List;

public class Car {
    private String brand;
    private String model;
    private int year;
    private int price;
    private int owner;
    private FuelType fuelType;
    private String VIN;
    private Color color;

    private List<String> images = new ArrayList<>();
    private Integer id;

    public Car(int id, String brand, String model, int year, int price,int owner, String fuelType, String VIN, String color ) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.owner = owner;
        this.fuelType = FuelType.valueOf(fuelType);
        this.VIN = VIN;
        this.color = Color.valueOf(color);
    }

    public Integer getId() { return id; }
    public int getOwner() {
        return owner;
    }
    public void setOwner(int owner) {
        this.owner = owner;
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

