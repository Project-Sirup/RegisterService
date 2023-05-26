package sirup.service.register.util;

public class Env {
    public static final int REG_PORT;
    public static final String REG_DB_ADDRESS;
    public static final int REG_DB_PORT;
    public static final String LOG_ADDRESS;
    public static final int LOG_PORT;
    public static final String AUTH_ADDRESS;
    public static final int AUTH_PORT;
    static {
        REG_PORT = Integer.parseInt(System.getenv("REG_PORT"));
        REG_DB_ADDRESS = System.getenv("REG_DB_ADDRESS");
        REG_DB_PORT = Integer.parseInt(System.getenv("REG_DB_PORT"));
        LOG_ADDRESS = System.getenv("LOG_ADDRESS");
        LOG_PORT = Integer.parseInt(System.getenv("LOG_PORT"));
        AUTH_ADDRESS = System.getenv("AUTH_ADDRESS");
        AUTH_PORT = Integer.parseInt(System.getenv("AUTH_PORT"));
    }
}
