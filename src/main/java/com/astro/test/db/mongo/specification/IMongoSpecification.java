package com.astro.test.db.mongo.specification;

import com.astro.test.db.ISpecification;
import org.bson.conversions.Bson;

/**
 * Created by sahanlm on 3/23/18.
 */
public interface IMongoSpecification extends ISpecification {

    Bson toMongoQuery();

}
