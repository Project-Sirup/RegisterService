package sirup.service.register.api;

import sirup.service.log.rpc.client.LogClient;
import sirup.service.register.controller.RegisterController;
import sirup.service.register.util.Env;

import static spark.Spark.*;

public class Api {

    private final LogClient logger = LogClient.getInstance();

    public void start() {
        port(Env.PORT);
        path("/api/v1", () -> {
            RegisterController registerController = new RegisterController();
            post("",    registerController::register);
            get("",     registerController::getRegister);
            delete("/:serviceId",  registerController::unregister);
        });
        logger.info("Server started, listening on 2100");
    }

}
