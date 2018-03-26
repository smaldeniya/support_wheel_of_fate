package com.astro.test.db.mongo.specification;

import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * Created by sahanlm on 3/26/18.
 */
public class FindEngineersByIdListSpecification implements IMongoSpecification {

    private List<String> idList;

    public FindEngineersByIdListSpecification(List<String> idList) {
        this.idList = idList;
    }


    public Bson toMongoQuery() {
        Bson query = Filters.in("id", this.idList);
        return  query;
    }
}
