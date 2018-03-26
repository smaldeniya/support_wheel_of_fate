package com.astro.test.db.mongo.specification;

import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by sahanlm on 3/26/18.
 */
public class FindSchedulesBetweenTwoDaysForUserSpecification implements IMongoSpecification {

    private Date from;
    private Date to;
    private String userId;

    public FindSchedulesBetweenTwoDaysForUserSpecification(Date from, Date to, String userId) {
        this.from = from;
        this.to = to;
        this.userId = userId;
    }


    public Bson toMongoQuery() {
        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(from);

        Calendar toCal = Calendar.getInstance();
        toCal.setTime(to);

        Bson query = Filters.and(
            Filters.and(
                Filters.gte("year", fromCal.get(Calendar.YEAR)),
                Filters.lte("year", toCal.get(Calendar.YEAR))
            ),
            Filters.and(
                Filters.gte("month", fromCal.get(Calendar.MONTH) + 1),
                Filters.lte("month", toCal.get(Calendar.MONTH) + 1)
            ),
            Filters.and(
                Filters.gte("date", fromCal.get(Calendar.DATE)),
                Filters.lte("date", toCal.get(Calendar.DATE))
            ),
            Filters.or(
                Filters.eq("firstHalfEngineerId", this.userId),
                Filters.eq("secondHalfEngineerId", this.userId)
            )
        );

        BsonDocument bsonDocument = query.toBsonDocument(BsonDocument.class, MongoClient.getDefaultCodecRegistry());
        return query;
    }
}
