package sirup.service.register.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import sirup.service.register.util.Env;

public class MongoDB {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public boolean connect() {
        try {
            this.mongoClient = MongoClients.create(Env.REG_DB_ADDRESS + ":" + Env.REG_DB_PORT);
            this.mongoDatabase = mongoClient.getDatabase("sirupReg");
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    public MongoDatabase getMongoDatabase() {
        return this.mongoDatabase;
    }
}
