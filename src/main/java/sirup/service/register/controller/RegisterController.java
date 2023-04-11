package sirup.service.register.controller;

import com.google.gson.Gson;
import sirup.service.log.rpc.client.LogClient;
import sirup.service.register.database.MongoDB;
import sirup.service.register.model.Registration;
import sirup.service.register.service.RegisterService;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static sirup.service.log.rpc.client.ColorUtil.*;

public class RegisterController {

    private final LogClient logger = LogClient.getInstance();
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
        String serviceId = this.service.create(registerRequest.registration()).orElse("NOT_AN_ID");
        logger.info("Registering service [" + id(serviceId) + "](" + registerRequest.registration().serviceName() + ")");
        return serviceId;
    }
    private record RegisterRequest(Registration registration) {}

    public Object unregister(Request request, Response response) {
        String serviceId = request.params("serviceId");
        logger.info("Unregistering service [" + id(serviceId) + "]");
        return this.service.remove(serviceId);
    }

    public Object getRegister(Request request, Response response) {
        List<Registration> registrations = this.service.findAll().into(new ArrayList<>()).stream().map(document ->
                new Registration(
                        (String) document.get("serviceName"),
                        (String) document.get("serviceAddress"),
                        (String) document.get("serviceId"),
                        document.get("manifest"))).collect(Collectors.toList());
        return this.gson.toJson(new ReturnObj<>(200,"registrations found",registrations));
    }

    private record ReturnObj<T>(int statusCode, String message, T data) {

    }
}
