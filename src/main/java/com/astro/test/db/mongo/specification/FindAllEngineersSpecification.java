package com.astro.test.db.mongo.specification;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * Created by sahanlm on 3/23/18.
 */
public class FindAllEngineersSpecification implements IMongoSpecification {
    public Bson toMongoQuery() {
        Bson query = new Document();
        return query;
    }
}
