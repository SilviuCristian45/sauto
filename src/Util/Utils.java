package Util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Utils {
    public static Map<String, String> parseQuery(String query) throws IOException {
        Map<String, String> result = new HashMap<>();
        if (query != null) {
            // Split the query string by the & symbol
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                // Split each pair by the = symbol
                String[] kv = pair.split("=");
                // Decode the key and value using UTF-8 encoding
                String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
                // Put the key and value in the result map
                result.put(key, value);
            }
        }
        return result;
    }

    public static void sendResponse(HttpExchange httpExchange, String responseData) throws IOException {
        OutputStream os = httpExchange.getResponseBody();
        httpExchange.sendResponseHeaders(200, responseData.length());
        os.write(responseData.getBytes());
        os.close();
    }

    public static Optional<String> getQueryParamValue(URI uri, String key) throws IOException {
        String query = uri.getQuery();
        Map<String, String> params = parseQuery(query);
        return Optional.ofNullable(params.get(key));
    }
}
