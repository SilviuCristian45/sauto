import Controller.CarController;
import Controller.PersonController;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(port), 0);

        server.createContext("/users", new PersonController());
        server.createContext("/cars", new CarController());

        server.setExecutor(null);

        System.out.println("Server is running on port " + port);
        server.start();
    }
}