package com.astro.test.db.mongo.repository;

import com.astro.test.db.dto.DaySchedule;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by sahanlm on 3/25/18.
 */
public class DayScheduleMongoRepositoryTest {

    private static final String TEST_MONGO_ID = new ObjectId().toString(); // _id
    private static final Calendar SCHEDULE_DATE = Calendar.getInstance();

    @Mock
    private DayScheduleMongoRepository mongoRepository;
    private DaySchedule daySchedule;

    @Before
    public void setup() {
        this.daySchedule = new DaySchedule(new ObjectId().toString(), "Test Eng 1", "Test Eng 2",
                SCHEDULE_DATE.get(Calendar.YEAR), SCHEDULE_DATE.get(Calendar.MONTH) + 1, SCHEDULE_DATE.get(Calendar.DATE));

        MockitoAnnotations.initMocks(this);
        Mockito.when(this.mongoRepository.add(this.daySchedule)).thenReturn(TEST_MONGO_ID);
    }

    @Test
    public void testAdd() {
        String id = this.mongoRepository.add(this.daySchedule);
        Assert.assertEquals(TEST_MONGO_ID, id);
        Mockito.verify(this.mongoRepository, Mockito.times(1)).add(this.daySchedule);
    }

    @Test
    public void testUpdate() {
        this.mongoRepository.update(this.daySchedule);
        Mockito.verify(this.mongoRepository, Mockito.times(1)).update(this.daySchedule);
    }

    @Test
    public void testRemove() {
        this.mongoRepository.remove(this.daySchedule);
        Mockito.verify(this.mongoRepository, Mockito.times(1)).remove(this.daySchedule);
    }

    @Test
    public void testAddAll() {
        List<DaySchedule> list = new ArrayList<DaySchedule>();
        list.add(this.daySchedule);
        this.mongoRepository.addAll(list);
        Mockito.verify(this.mongoRepository, Mockito.times(1)).addAll(list);
    }
}
