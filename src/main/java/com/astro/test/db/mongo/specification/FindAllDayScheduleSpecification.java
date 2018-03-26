package com.astro.test.db.mongo.specification;

import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * Created by sahanlm on 3/26/18.
 */
public class FindAllDayScheduleSpecification implements IMongoSpecification {

    public Bson toMongoQuery() {
        return new Document();
    }

}
