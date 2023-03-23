package sirup.service.register.controller;

import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class RegisterController {

    private final Map<String, String> registerMap;

    public RegisterController() {
        this.registerMap = new HashMap<>();
    }

    public Object register(Request request, Response response) {
        return "not implemented";
    }

    public Object unregister(Request request, Response response) {
        return "not implemented";
    }

    public Object getRegister(Request request, Response response) {
        return "not implemented";
    }
}
