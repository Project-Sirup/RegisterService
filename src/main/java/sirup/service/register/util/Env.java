package sirup.service.register.util;

import io.github.cdimascio.dotenv.Dotenv;

public class Env {
    public static final int PORT;
    static {
        Dotenv dotenv = Dotenv.load();
        PORT = Integer.parseInt(dotenv.get("PORT"));
    }
}
