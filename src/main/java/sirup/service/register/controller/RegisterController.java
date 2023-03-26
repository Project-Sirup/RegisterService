package sirup.service.register.controller;

import com.google.gson.Gson;
import sirup.service.register.database.MongoDB;
import sirup.service.register.model.Registration;
import sirup.service.register.service.RegisterService;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RegisterController {

    private final RegisterService service;
    private final Gson gson;

    public RegisterController() {
        MongoDB mongoDB = new MongoDB();
        mongoDB.connect();
        this.service = new RegisterService(mongoDB.getMongoDatabase());
        this.gson = new Gson();
    }

    public Object register(Request request, Response response) {
        RegisterRequest registerRequest = this.gson.fromJson(request.body(), RegisterRequest.class);
        return this.service.create(registerRequest.registration());
    }
    private record RegisterRequest(Registration registration) {}

    public Object unregister(Request request, Response response) {
        RegisterRequest registerRequest = this.gson.fromJson(request.body(), RegisterRequest.class);
        return this.service.remove(registerRequest.registration());
    }

    public Object getRegister(Request request, Response response) {
        List<Registration> registrations = this.service.findAll().into(new ArrayList<>()).stream().map(document ->
                new Registration(
                        (String) document.get("serviceName"),
                        (String) document.get("serviceAddress"),
                        document.get("manifest"))).collect(Collectors.toList());
        return this.gson.toJson(registrations);
    }
}
