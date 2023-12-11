package Controller;

import Firebase.FirebaseConfig;
import Model.Car;
import Model.Person;
import Util.Repositories;
import Util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import Util.CarSerializer;

import javax.servlet.MultipartConfigElement;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CarController implements HttpHandler {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Car.class, new CarSerializer()).create();
    private static final Logger LOGGER = Logger.getLogger(CarController.class.getName());

    private final FirebaseConfig firebaseConfig;

    public CarController() throws IOException {
        firebaseConfig = new FirebaseConfig();

    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            httpExchange.setAttribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            handleResponse(httpExchange);
        } catch (Exception err) {
            System.out.println(err.toString());
        }
    }

    private void handleResponse(HttpExchange httpExchange) throws IOException, FileUploadException {
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

                String contentType = httpExchange.getRequestHeaders().getFirst("Content-Type");


                if (contentType.equals("application/json")) {
                    String requestBody = new String(httpExchange.getRequestBody().readAllBytes());
                    Car carToAdd = gson.fromJson(requestBody, Car.class);
                    Repositories.carRepository.addCar(carToAdd, person);
                    Utils.sendResponse(httpExchange, requestBody);
                } else {
                    FileItemFactory factory = new DiskFileItemFactory();
                    // Create a new file upload handler
                    ServletFileUpload upload = new ServletFileUpload(factory);
                    // Parse the request to get file items
                    List<FileItem> fileItems = upload.parseRequest(new HttpExchangeRequestContext(httpExchange));
                    // Process the file items
                    List<String> imageURLs = new ArrayList<>();
                    for (FileItem fileItem : fileItems) {
                        if (!fileItem.isFormField() && "image".equals(fileItem.getFieldName())) {
                            InputStream fileInputStream = fileItem.getInputStream();
                            imageURLs.add(firebaseConfig.uploadImage(fileItem.getName(), fileInputStream));
                        }
                    }
                    imageURLs.forEach( imageURL -> Repositories.carRepository.addImageToCar(imageURL, id));
                    Utils.sendResponse(httpExchange, imageURLs.toString());
                }
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

    // Custom HttpExchangeRequestContext class to adapt HttpExchange to FileUpload API
    class HttpExchangeRequestContext implements RequestContext {
        private final HttpExchange httpExchange;

        public HttpExchangeRequestContext(HttpExchange httpExchange) {
            this.httpExchange = httpExchange;
        }

        @Override
        public String getCharacterEncoding() {
            return httpExchange.getRequestHeaders().getFirst("Content-Encoding");
        }

        @Override
        public String getContentType() {
            return httpExchange.getRequestHeaders().getFirst("Content-Type");
        }

        @Override
        public int getContentLength() {
            String length = httpExchange.getRequestHeaders().getFirst("Content-Length");
            return (length != null) ? Integer.parseInt(length) : 0;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return httpExchange.getRequestBody();
        }
    }
}
