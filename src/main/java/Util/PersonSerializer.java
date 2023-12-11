package Util;

import Model.Car;
import Model.Person;
import com.google.gson.*;

import java.lang.reflect.Type;

public class PersonSerializer implements JsonSerializer<Person> {

    @Override
    public JsonElement serialize(Person person, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", person.getId());
        jsonObject.addProperty("username", person.getUsername());
        jsonObject.addProperty("email", person.getEmail());
        jsonObject.addProperty("password", person.getPassword());
        JsonArray carsArray = new JsonArray();
        for (Car car : person.getCars()) {
            JsonObject carObject = new JsonObject();
            carObject.addProperty("brand", car.getBrand());
            carObject.addProperty("model", car.getModel());
            carObject.addProperty("year", car.getYear());
            carObject.addProperty("price", car.getPrice());
            carsArray.add(carObject);
        }
        jsonObject.add("cars", carsArray);
        return jsonObject;
    }
}
