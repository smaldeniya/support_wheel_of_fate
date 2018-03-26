package com.astro.test.db.mongo.specification;

import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

/**
 * Created by sahanlm on 3/23/18.
 */
public class FindEngineersByIdSpecification implements IMongoSpecification {

    private String id;

    public FindEngineersByIdSpecification(String  id) {
        this.id = id;
    }


    public Bson toMongoQuery() {
        Bson query = Filters.eq("id", id);
        return query;
    }
}
