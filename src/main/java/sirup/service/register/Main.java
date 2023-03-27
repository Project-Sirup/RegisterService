package sirup.service.register;

import sirup.service.log.rpc.client.LogClient;
import sirup.service.register.api.Api;

public class Main {
    public static void main(String[] args) {
        LogClient.init("localhost", 2102, "RegisterService");
        new Api().start();
    }
}
