package Controller;

import Model.Car;
import Model.Person;
import Util.Repositories;
import Util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;
import Util.CarSerializer;

public class CarController implements HttpHandler {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Car.class, new CarSerializer()).create();
    private static final Logger LOGGER = Logger.getLogger(CarController.class.getName());

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            handleResponse(httpExchange);
        } catch (Exception err) {
            System.out.println(err.toString());
        }
    }

    private void handleResponse(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        Optional<String> stringId = Utils.getQueryParamValue(httpExchange.getRequestURI(), "id");
        switch (method) {
            case "GET": {
                if (stringId.isEmpty()) {
                    LOGGER.severe("query parameter not provided");
                    Utils.sendResponse( httpExchange, gson.toJson(Repositories.carRepository.getCars()));
                    break;
                }
                int id = Integer.parseInt(stringId.get());
                var user = Repositories.personRepository.getUserById(id);
                if (user.isEmpty()) {
                    LOGGER.severe("not found a user");
                    Utils.sendResponse(404, httpExchange, "not found a user");
                } else {
                    Person person = user.get();
                    Utils.sendResponse(httpExchange, gson.toJson(person.getCars()));
                }
                break;
            }
            case "POST": {
                if (stringId.isEmpty()) {
                    LOGGER.severe("query parameter not provided");
                    Utils.sendResponse(404, httpExchange, "query parameter not provided");
                    break;
                }
                int id = Integer.parseInt(stringId.get());
                Optional<Person> optionalPerson = Repositories.personRepository.getUserById(id);
                if (optionalPerson.isEmpty()) {
                    LOGGER.severe("NO PERSON MATCHING THE GIVEN ID");
                    Utils.sendResponse(404, httpExchange, "NO PERSON MATCHING THE GIVEN ID");
                    break;
                }
                Person person = optionalPerson.get();
                String requestBody = new String(httpExchange.getRequestBody().readAllBytes());
                Car carToAdd = gson.fromJson(requestBody, Car.class);
                Repositories.carRepository.addCar(carToAdd, person);
                Utils.sendResponse(httpExchange, requestBody);
            }
                break;
            case "DELETE":
                if (stringId.isEmpty()) {
                    LOGGER.severe("query parameter not provided");
                    Utils.sendResponse(404, httpExchange, "query parameter not provided");
                    break;
                }
                int id = Integer.parseInt(stringId.get());
                Repositories.carRepository.deleteCarById(id);
                Repositories.personRepository.deleteUserCarById(id);
                Utils.sendResponse(httpExchange, "car has been deleted");
                break;
        }
    }


}
