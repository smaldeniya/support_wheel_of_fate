package com.astro.test.db.mongo.repository;

import com.astro.test.db.IDbUtil;
import com.astro.test.db.IRepository;
import com.astro.test.db.ISpecification;
import com.astro.test.db.dto.DaySchedule;
import com.astro.test.db.mongo.MongoDbUtil;
import com.astro.test.db.mongo.specification.IMongoSpecification;
import com.astro.test.util.Constants;
import com.google.gson.Gson;
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
 * Created by sahanlm on 3/25/18.
 */
public class DayScheduleMongoRepository implements IRepository<DaySchedule> {

    public static final String COLLECTION_NAME = "day_schedule";

    private MongoDatabase database;
    private MongoCollection<Document> dayScheduleCollection;

    public DayScheduleMongoRepository() throws IOException {
        this.configure();
    }

    private void configure() throws IOException {
        IDbUtil<MongoDatabase> mongodbUtil = MongoDbUtil.getInstance();
        this.database = mongodbUtil.getDbInstance();
        this.dayScheduleCollection = this.database.getCollection(DayScheduleMongoRepository.COLLECTION_NAME);
    }


    public String add(DaySchedule item) {
        Document document = MongoDbUtil.convertToBasicDbObject(item);
        this.dayScheduleCollection.insertOne(document);
        return document.get(Constants.MONGO_ID).toString();
    }

    public void addAll(List<DaySchedule> items) {
        List<Document> documentList = new ArrayList<Document>();
        for (DaySchedule d: items) {
            documentList.add(MongoDbUtil.convertToBasicDbObject(d));
        }
        this.dayScheduleCollection.insertMany(documentList);
    }

    public void update(DaySchedule item) {
        Document document = MongoDbUtil.convertToBasicDbObject(item);
        Bson query = Filters.eq("id", item.getId());
        this.dayScheduleCollection.replaceOne(query, document);
    }

    public void remove(DaySchedule item) {
        Bson query = Filters.eq("id", item.getId());
        this.dayScheduleCollection.deleteOne(query);
    }

    public List<DaySchedule> query(ISpecification specification) {
        if (!(specification instanceof IMongoSpecification)) throw new RuntimeException("Specification type unsupported");

        List<DaySchedule> scheduleList = new ArrayList<DaySchedule>();
        Gson gson = new Gson();

        IMongoSpecification mongoSpecification = (IMongoSpecification) specification;
        MongoCursor<Document> result = this.dayScheduleCollection.find(mongoSpecification.toMongoQuery()).iterator();

        while (result.hasNext()) {
            Document doc = result.next();
            scheduleList.add(gson.fromJson(doc.toJson(), DaySchedule.class));
        }

        return scheduleList;
    }
}
