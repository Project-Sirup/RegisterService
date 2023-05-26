package sirup.service.register;

import sirup.service.auth.rpc.client.AuthClient;
import sirup.service.log.rpc.client.LogClient;
import sirup.service.register.api.Api;
import sirup.service.register.util.Env;

public class Main {
    public static void main(String[] args) {
        LogClient.init(Env.LOG_ADDRESS, Env.LOG_PORT, "RegisterService");
        AuthClient.init(Env.AUTH_ADDRESS,Env.AUTH_PORT);
        new Api().start();
    }
}
