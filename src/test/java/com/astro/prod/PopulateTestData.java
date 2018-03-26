package com.astro.prod;

import com.astro.test.db.dto.Engineer;
import com.astro.test.db.mongo.MongoDbUtil;
import com.astro.test.db.mongo.repository.DayScheduleMongoRepository;
import com.astro.test.db.mongo.repository.EngineerMongoRepository;
import com.astro.test.db.mongo.specification.FindAllEngineersSpecification;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by sahanlm on 3/25/18.
 */
public class PopulateTestData {

    private EngineerMongoRepository engineerMongoRepository;

    @Before
    public void setup() throws IOException {

        MongoDatabase database = MongoDbUtil.getInstance().getDbInstance();
        MongoCollection engCollection = database.getCollection(EngineerMongoRepository.COLLECTION_NAME);
        MongoCollection scheduleColelction = database.getCollection(DayScheduleMongoRepository.COLLECTION_NAME);

        engCollection.deleteMany(new Document());
        scheduleColelction.deleteMany(new Document());

        engineerMongoRepository = new EngineerMongoRepository();

        List<Engineer> currentEngineers = this.engineerMongoRepository.query(new FindAllEngineersSpecification());

        if (currentEngineers.size() < 10) {
            int requiredNumber = 10 - currentEngineers.size();
            int offset = currentEngineers.size();
            for (int i=0; i < requiredNumber; i++) {
                offset +=1;
                Engineer engineer = new Engineer("Test Engineer " + offset, new ObjectId().toString());
                this.engineerMongoRepository.add(engineer);
            }
        }
    }

    @Test
    public void testCreateProdData() {
        List<Engineer> engineerList = this.engineerMongoRepository.query(new FindAllEngineersSpecification());
        Assert.assertEquals(engineerList.size(), 10);
    }

}
