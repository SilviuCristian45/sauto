package Util;

import com.github.shyiko.dotenv.DotEnv;

import java.util.Map;

public class EnvConfig {
    private final Map<String, String> dotEnv = DotEnv.load();

    public String getUser() {
        return dotEnv.get("DB_USER");
    }

    public String getPassword() {
        return dotEnv.get("PASSWORD");
    }

    public String getURL() {
        return dotEnv.get("URL");
    }
}