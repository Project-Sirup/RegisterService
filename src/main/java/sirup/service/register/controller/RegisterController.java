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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

        logger.debug("attempting register of " + serviceId);

        if(!auth.auth(serviceToken, serviceId, SystemAccess.SERVICE.id)){
            return this.gson.toJson(new ReturnObj<>(498, "invalid service credentials"));
        }
        if(!this.service.create(registerRequest.registration())) {
            return this.gson.toJson(new ReturnObj<>(400,"could not add registration"));
        }
        logger.info("Registering service [" + id(registerRequest.registration().serviceId()) + "]" +
                "(" + registerRequest.registration().serviceName() + ")");
        try {
            notifyUsers(registerRequest.registration().serviceName());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
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

    private record InviteNotificationObject(String eventType, String message) {}
    private void notifyUsers(String serviceName) throws URISyntaxException, IOException, InterruptedException {
        //System.out.println("Notifying " + invite.receiverId());
        InviteNotificationObject ino = new InviteNotificationObject("global", "New generation service added: " + serviceName);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://notificationservice:2104/api/v1/trigger"))
                .setHeader("Content-Type","Application/json")
                .POST(HttpRequest.BodyPublishers.ofString(this.gson.toJson(ino)))
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}
