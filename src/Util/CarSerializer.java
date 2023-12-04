package Util;

import Model.Car;
import Model.Person;
import com.google.gson.*;

public class CarSerializer implements JsonSerializer<Car> {
    @Override
    public JsonElement serialize(Car car, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", car.getId());
        jsonObject.addProperty("brand", car.getBrand());
        jsonObject.addProperty("model", car.getModel());
        jsonObject.addProperty("year", car.getYear());
        jsonObject.addProperty("price", car.getPrice());
        return jsonObject;
    }
}
