package com.astro.test.db.mongo.repository;

import com.astro.test.db.dto.DaySchedule;
import com.astro.test.db.dto.Engineer;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sahanlm on 3/23/18.
 */
public class EngineerMongoRepositoryTest{

    private static final String TEST_MONGO_ID = "5ab4c747b770b331e19aa1fc";

    @Mock
    private EngineerMongoRepository mongoRepository;
    private Engineer engineer;

    @Before
    public void setup() throws IOException {
        this.engineer = new Engineer();
        this.engineer.setName("Test");
        this.engineer.setId(new ObjectId().toString());

        MockitoAnnotations.initMocks(this);
        Mockito.when(this.mongoRepository.add(this.engineer)).thenReturn(TEST_MONGO_ID);
    }

    @Test
    public void testAdd() {
        String _id = this.mongoRepository.add(this.engineer);
        Assert.assertEquals(TEST_MONGO_ID, _id);
        Mockito.verify(this.mongoRepository, Mockito.times(1)).add(this.engineer);
    }

    @Test
    public void testUpdate() {
        this.mongoRepository.update(this.engineer);
        Mockito.verify(this.mongoRepository, Mockito.times(1)).update(this.engineer);
    }

    @Test
    public void testRemove() {
        this.mongoRepository.remove(this.engineer);
        Mockito.verify(this.mongoRepository, Mockito.times(1)).remove(this.engineer);
    }

    @Test
    public void testAddAll() {
        List<Engineer> list = new ArrayList<Engineer>();
        list.add(this.engineer);
        this.mongoRepository.addAll(list);
        Mockito.verify(this.mongoRepository, Mockito.times(1)).addAll(list);
    }

}
