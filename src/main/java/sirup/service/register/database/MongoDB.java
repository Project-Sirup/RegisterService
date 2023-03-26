package sirup.service.register.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDB {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public boolean connect() {
        try {
            this.mongoClient = MongoClients.create("mongodb://localhost:27017");
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
