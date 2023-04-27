package sirup.service.register;

import sirup.service.auth.rpc.client.AuthClient;
import sirup.service.log.rpc.client.LogClient;
import sirup.service.register.api.Api;

public class Main {
    public static void main(String[] args) {
        LogClient.init("localhost", 2102, "RegisterService");
        AuthClient.init("localhost",2101);
        new Api().start();
    }
}
