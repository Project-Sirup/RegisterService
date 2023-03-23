package sirup.service.register.api;

import sirup.service.register.controller.RegisterController;

import static spark.Spark.*;

public class Api {

    public void start() {
        port(2100);
        path("/api/v1", () -> {
            RegisterController registerController = new RegisterController();
            post("",    registerController::register);
            get("",     registerController::getRegister);
            delete("",  registerController::unregister);
        });
    }
}
