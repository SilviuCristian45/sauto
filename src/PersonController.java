import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PersonController implements HttpHandler {

    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange httpExchange) {
        try {
            handleResponse(httpExchange);
        } catch (Exception err) {
            System.out.println(err.toString());
        }
    }

    private void handleResponse(HttpExchange httpExchange) throws IOException {
        System.out.println(httpExchange.getRequestMethod());
        URI uri = httpExchange.getRequestURI();
        Optional<String> idString = (Utils.getQueryParamValue(uri, "id"));
        int id = idString.map(Integer::parseInt).orElse(-1);
        String requestBody = new String(httpExchange.getRequestBody().readAllBytes());

        switch (httpExchange.getRequestMethod()) {
            case "POST":
                Person person = gson.fromJson(requestBody, Person.class); // deserializes json into target2
                Repositories.personRepository.registerUser(person);
                Utils.sendResponse(httpExchange, requestBody);
                break;

            case "GET":

                Optional<Person> userFound = Repositories.personRepository.getUserById(id);
                if (userFound.isPresent()) {
                    String userJson = gson.toJson(userFound);
                    Utils.sendResponse(httpExchange, userJson);
                } else {
                    String usersJson = gson.toJson(Repositories.personRepository.getUsers());
                    Utils.sendResponse(httpExchange, usersJson);
                }
                break;

            case "DELETE":
                boolean userWasDeleted = Repositories.personRepository.deleteUserById(id);
                Utils.sendResponse(httpExchange, gson.toJson(userWasDeleted));
                break;
            case "PATCH":
                Map<String, String> updateFields = gson.fromJson(requestBody, Map.class);
                Optional<Person> existingUser = Repositories.personRepository.getUserById(id);

                if (existingUser.isPresent()) {
                    Person personToUpdate = existingUser.get();
                    updateFields.forEach( (key, val) -> {
                                if (key.equals("username")) {
                                    personToUpdate.setUsername(val);
                                } else if (key.equals("email")) {
                                    personToUpdate.setEmail(val);
                                }
                            }
                    );
                    Repositories.personRepository.updateUser(personToUpdate);
                    Utils.sendResponse(httpExchange, "user updated succesfully");
                } else {
                    Utils.sendResponse(httpExchange, "User not updated");
                }
                break;
        }
    }
}
