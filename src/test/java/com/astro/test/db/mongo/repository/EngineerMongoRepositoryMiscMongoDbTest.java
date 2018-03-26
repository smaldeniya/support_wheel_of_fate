package com.astro.test.db.mongo.repository;

import com.astro.test.db.dto.Engineer;
import com.astro.test.db.mongo.specification.FindEngineersByIdSpecification;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by sahanlm on 3/23/18.
 */
public class EngineerMongoRepositoryMiscMongoDbTest {

    private static EngineerMongoRepository mongoRepository;

    @BeforeClass
    public static void setup() throws IOException {
        mongoRepository = new EngineerMongoRepository();
    }

    @Test
    public void testAddUpdateRemove() {
        Engineer engineer = new Engineer();
        engineer.setName("Test Engineer");
        engineer.setId(new ObjectId().toString());

        String id = mongoRepository.add(engineer);
        Assert.assertNotNull(id);
        List<Engineer> engineerList = mongoRepository.query(new FindEngineersByIdSpecification(engineer.getId()));
        Assert.assertEquals(engineerList.size(), 1);
        Engineer foundEngineer = engineerList.get(0);
        Assert.assertEquals(foundEngineer.getName(), engineer.getName());

        engineer.setName("Test 2");
        mongoRepository.update(engineer);
        engineerList = mongoRepository.query(new FindEngineersByIdSpecification(engineer.getId()));
        Assert.assertEquals(engineerList.size(), 1);
        foundEngineer = engineerList.get(0);
        Assert.assertNotEquals(foundEngineer.getName(), "Test Engineer");
        Assert.assertEquals(foundEngineer.getName(), "Test 2");

        mongoRepository.remove(engineer);
    }
}
