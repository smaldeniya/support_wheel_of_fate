package com.astro.test.service;

import com.astro.test.db.IDbUtil;
import com.astro.test.db.IRepository;
import com.astro.test.db.dto.DaySchedule;
import com.astro.test.db.dto.Engineer;
import com.astro.test.db.mongo.MongoDbUtil;
import com.astro.test.db.mongo.repository.DayScheduleMongoRepository;
import com.astro.test.db.mongo.repository.EngineerMongoRepository;
import com.astro.test.db.mongo.specification.FindSchedulesBetweenTwoDaysForUserSpecification;
import com.astro.test.db.mongo.specification.FindSchedulesBetweenTwoDaysSpecification;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.junit.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by sahanlm on 3/26/18.
 */
public class AnEnginnerShouldHaveCompleteTwoShiftsWithinFortNightTest {

    private static final Logger logger = LogManager.getLogger(AnEnginnerShouldHaveCompleteTwoShiftsWithinFortNightTest.class);

    private static IRepository<Engineer> engineerIRepository;
    private static IRepository<DaySchedule> scheduleIRepository;
    private static MongoDatabase database;
    private static IScheduleService scheduleService;

    private List<Engineer> engineerList;

    @BeforeClass
    public static void init() throws IOException {
        engineerIRepository = new EngineerMongoRepository();
        scheduleIRepository = new DayScheduleMongoRepository();

        IDbUtil<MongoDatabase> databaseIDbUtil = MongoDbUtil.getInstance();
        database = databaseIDbUtil.getDbInstance();

        scheduleService = new ScheduleServiceImpl();
    }

    @Before
    public void setup() {
        engineerList = new ArrayList<Engineer>();
        for (int i = 0; i < 10; i++) {
            Engineer eng = new Engineer("Test Engineer" + i, "e"+(i+1));
            engineerList.add(eng);
        }
        engineerIRepository.addAll(engineerList);
    }

    @After
    public void tearDown() {
        MongoCollection<Document> engineerCollection = database.getCollection(EngineerMongoRepository.COLLECTION_NAME);
        MongoCollection<Document> scheduleCollection = database.getCollection(DayScheduleMongoRepository.COLLECTION_NAME);

        engineerCollection.deleteMany(new Document());
        scheduleCollection.deleteMany(new Document());
    }

    @Test
    public void testCondition() {
        Date today = new Date();
        Calendar to = Calendar.getInstance();
        to.setTime(today);

        Calendar from = Calendar.getInstance();
        from.setTime(today);
        from.add(Calendar.DATE, -10);

        Calendar date = Calendar.getInstance();
        date.setTime(from.getTime());
        for (int i = 0; i < 10; i++) {
            DaySchedule schedule = scheduleService.getDayScheduleFor(date.getTime());
            date.add(Calendar.DATE, 1);
        }

        for (Engineer eng: engineerList) {
            List<DaySchedule> enginerScheduleForTwoWeeks = scheduleIRepository.query(
                    new FindSchedulesBetweenTwoDaysForUserSpecification(from.getTime(), to.getTime(), eng.getId())
            );
            Assert.assertTrue(enginerScheduleForTwoWeeks.size() <= 2);
        }
    }
}
