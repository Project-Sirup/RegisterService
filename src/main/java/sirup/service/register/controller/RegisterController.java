package sirup.service.register.controller;

import com.google.gson.Gson;
import sirup.service.auth.rpc.client.AuthClient;
import sirup.service.auth.rpc.client.SystemAccess;
import sirup.service.log.rpc.client.LogClient;
import sirup.service.register.database.MongoDB;
import sirup.service.register.model.Registration;
import sirup.service.register.service.RegisterService;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static sirup.service.log.rpc.client.ColorUtil.*;

public class RegisterController {

    private final LogClient logger = LogClient.getInstance();
    private final RegisterService service;
    private final Gson gson;
    private final AuthClient auth = AuthClient.getInstance();

    public RegisterController() {
        MongoDB mongoDB = new MongoDB();
        mongoDB.connect();
        this.service = new RegisterService(mongoDB.getMongoDatabase());
        this.gson = new Gson();
    }

    public Object register(Request request, Response response) {
        RegisterRequest registerRequest = this.gson.fromJson(request.body(), RegisterRequest.class);
        String serviceToken = registerRequest.serviceToken();
        String serviceId = registerRequest.registration().serviceId();

        if(!auth.auth(serviceToken, serviceId, SystemAccess.SERVICE.id)){
            return this.gson.toJson(new ReturnObj<>(498, "invalid service credentials"));
        }
        if(!this.service.create(registerRequest.registration())) {
            return this.gson.toJson(new ReturnObj<>(400,"could not add registration"));
        }
        logger.info("Registering service [" + id(registerRequest.registration().serviceId()) + "]" +
                "(" + registerRequest.registration().serviceName() + ")");
        return this.gson.toJson(new ReturnObj<>(201,"registration added"));
    }
    private record RegisterRequest(Registration registration, String serviceToken) {}

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
        private ReturnObj(int statusCode, String message) {
            this(statusCode, message, null);
        }
    }
}
