package com.astro.test.db.mongo;

import com.astro.test.db.IDbUtil;
import com.astro.test.db.dto.Engineer;
import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import junit.framework.TestCase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by sahanlm on 3/23/18.
 */
public class MongoDbUtilTest extends TestCase {

    @Test
    public void testGetMongoDbUtilInstance() throws IOException {
        IDbUtil<MongoDatabase> mongoDbUtil = MongoDbUtil.getInstance();
        assertNotNull(mongoDbUtil);
    }

    @Test
    public void testGetDatabaseInstanceForMongoDbUtil() throws IOException {
        IDbUtil<MongoDatabase> mongoDbUtil = MongoDbUtil.getInstance();
        MongoDatabase database = mongoDbUtil.getDbInstance();
        assertNotNull(database);
    }

    @Test
    public void testConvertToDbObject() throws IOException {
        Engineer e = new Engineer();
        e.setId(new ObjectId().toString());
        e.setName("Test");
        Document document = MongoDbUtil.convertToBasicDbObject(e);
        assertNotNull(document);

        Gson gson = new Gson();
        String json = JSON.serialize(document);
        Engineer result = gson.fromJson(json, Engineer.class);
        assertEquals(e.getId(), result.getId());
        assertEquals(e.getName(), result.getName());
    }

}
