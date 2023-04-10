package sirup.service.register.api;

import sirup.service.log.rpc.client.LogClient;
import sirup.service.log.rpc.client.LogServiceUnavailableException;
import sirup.service.register.controller.RegisterController;
import sirup.service.register.util.Env;

import java.util.Optional;

import static spark.Spark.*;

public class Api {

    private final LogClient logger = LogClient.getInstance();

    public void start() {
        port(Env.PORT);
        before("*",(request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Headers", "*");
            response.header("Access-Control-Allow-Credentials", "true");
            response.header("Access-Control-Allow-Credentials-Header", "*");
            response.header("Accept", "*/*");
            //response.header("Content-Type", "text/event-stream");
        });
        options("*", ((request, response) -> {
            Optional.ofNullable(request.headers("Access-Control-Request-Headers"))
                    .ifPresent(header -> response.header("Access-Control-Allow-Headers", header));
            Optional.ofNullable(request.headers("Access-Control-Request-Method"))
                    .ifPresent(header -> response.header("Access-Control-Allow-Methods", header));
            Optional.ofNullable(request.headers("Accept"))
                    .ifPresent(header -> response.header("Accept", header));
            response.status(200);
            return "OK";
        }));
        path("/api/v1", () -> {
            get("/health", (req, res) -> "ok");
            RegisterController registerController = new RegisterController();
            post("",    registerController::register);
            get("",     registerController::getRegister);
            delete("/:serviceId",  registerController::unregister);
        });
        logger.info("Server started, listening on 2100");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                logger.warn("Shutting down");
            } catch (LogServiceUnavailableException e) {
                System.err.println("Could not log shutdown");
            }
        }));
    }

}
