package com.astro.test.db.mongo.repository;

import com.astro.test.db.IDbUtil;
import com.astro.test.db.IRepository;
import com.astro.test.db.ISpecification;
import com.astro.test.db.dto.Engineer;
import com.astro.test.db.mongo.MongoDbUtil;
import com.astro.test.db.mongo.specification.IMongoSpecification;
import com.astro.test.util.Constants;
import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sahanlm on 3/23/18.
 */
public class EngineerMongoRepository implements IRepository<Engineer> {

    public static final String COLLECTION_NAME = "engineer";

    private MongoDatabase database;
    private MongoCollection<Document> employeeCollection;

    public EngineerMongoRepository() throws IOException {
        this.configure();
    }

    public String add(Engineer item) {
        Document document = MongoDbUtil.convertToBasicDbObject(item);
        this.employeeCollection.insertOne(document);
        return document.get(Constants.MONGO_ID).toString();
    }

    public void addAll(List<Engineer> items) {
        List<Document> documentList = new ArrayList<Document>();
        for (Engineer e: items) {
            documentList.add(MongoDbUtil.convertToBasicDbObject(e));
        }
        this.employeeCollection.insertMany(documentList);
    }

    public void update(Engineer item) {
        Document document = MongoDbUtil.convertToBasicDbObject(item);
        Bson query = Filters.eq("id", item.getId());
        this.employeeCollection.replaceOne(query, document);
    }

    public void remove(Engineer item) {
        Bson query = Filters.eq("id", item.getId());
        this.employeeCollection.deleteOne(query);
    }

    public List<Engineer> query(ISpecification specification) {
        if (!(specification instanceof IMongoSpecification)) throw new RuntimeException("Specification type unsupported");

        List<Engineer> engineerList = new ArrayList<Engineer>();
        Gson gson = new Gson();

        IMongoSpecification mongoSpecification = (IMongoSpecification) specification;
        MongoCursor<Document> result = this.employeeCollection.find(mongoSpecification.toMongoQuery()).iterator();

        while (result.hasNext()) {
            Document doc = result.next();
            engineerList.add(gson.fromJson(doc.toJson(), Engineer.class));
        }

        return engineerList;
    }

    private void configure() throws IOException {
        IDbUtil<MongoDatabase> mongodbUtil = MongoDbUtil.getInstance();
        this.database = mongodbUtil.getDbInstance();
        this.employeeCollection = this.database.getCollection(EngineerMongoRepository.COLLECTION_NAME);
    }

}
