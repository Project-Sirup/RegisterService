package sirup.service.register.service;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import sirup.service.register.controller.RegisterController;
import sirup.service.register.model.Registration;

import java.util.ArrayList;
import java.util.List;

public class RegisterService {

    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    public RegisterService(final MongoDatabase database) {
        this.database = database;
        this.collection = database.getCollection("registrations");
    }

    public boolean create(Registration registration) {
        Document queryDocument = new Document();
        queryDocument.put("serviceAddress", registration.serviceAddress());
        if (this.collection.find(queryDocument).into(new ArrayList<>()).size() > 0) {
            return false;
        }
        Document document = new Document();
        document.put("serviceName", registration.serviceName());
        document.put("serviceAddress", registration.serviceAddress());
        document.put("manifest", registration.manifest());
        this.collection.insertOne(document);
        return true;
    }
    public FindIterable<Document> findAll() {
        return this.collection.find();
    }
    public boolean remove(Registration registration) {
        Document queryDocument = new Document();
        queryDocument.put("serviceAddress", registration.serviceAddress());
        return this.collection.findOneAndDelete(queryDocument) != null;
    }
}
