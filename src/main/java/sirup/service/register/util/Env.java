package sirup.service.register.util;

public class Env {
    public static final int REG_PORT;
    public static final String REG_DB_ADDRESS;
    public static final int REG_DB_PORT;
    static {
        REG_PORT = Integer.parseInt(System.getenv("REG_PORT"));
        REG_DB_ADDRESS = System.getenv("REG_DB_ADDRESS");
        REG_DB_PORT = Integer.parseInt(System.getenv("REG_DB_PORT"));
    }
}
