package com.astro.test.service;

import com.astro.test.db.IDbUtil;
import com.astro.test.db.IRepository;
import com.astro.test.db.dto.DaySchedule;
import com.astro.test.db.dto.Engineer;
import com.astro.test.db.mongo.MongoDbUtil;
import com.astro.test.db.mongo.repository.DayScheduleMongoRepository;
import com.astro.test.db.mongo.repository.EngineerMongoRepository;
import com.astro.test.db.mongo.specification.FindDayScheduleForDateSpecification;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.*;

import java.io.IOException;
import java.util.*;

/**
 * Created by sahanlm on 3/25/18.
 */
public class SchechuleServiceImplTest {

    private static IRepository<Engineer> engineerIRepository;
    private static IRepository<DaySchedule> scheduleIRepository;

    private static MongoDatabase database;
    private Engineer engineer1;
    private Engineer engineer2;
    private Engineer engineer3;

    @BeforeClass
    public static void setup () throws IOException {
        engineerIRepository = new EngineerMongoRepository();
        scheduleIRepository = new DayScheduleMongoRepository();

        IDbUtil<MongoDatabase> databaseIDbUtil = MongoDbUtil.getInstance();
        database = databaseIDbUtil.getDbInstance();
    }

    @Test
    public void testGetEngineerScheduleFor() throws IOException {

        IScheduleService service = new ScheduleServiceImpl();
        Date today = new Date();
        List<Engineer> engineerList = service.getEngineerScheduledFor(today);
        Assert.assertNotNull(engineerList);
        Assert.assertEquals(engineerList.size(), 2);
        List<String> idList = new ArrayList<String>();
        idList.add(engineerList.get(0).getId());
        idList.add(engineerList.get(1).getId());

        List<DaySchedule> scheduleList = scheduleIRepository.query(new FindDayScheduleForDateSpecification(today));
        Assert.assertEquals(scheduleList.size(), 1);
        DaySchedule todaySchedule = scheduleList.get(0);
        Assert.assertTrue(idList.indexOf(todaySchedule.getFirstHalfEngineerId()) >= 0);
        Assert.assertTrue(idList.indexOf(todaySchedule.getSecondHalfEngineerId()) >= 0);

    }

    @Test
    public void testGetDayScheculeFor() throws IOException {
        IScheduleService service = new ScheduleServiceImpl();
        Date today = new Date();
        DaySchedule todaySchedule = service.getDayScheduleFor(today);
        Assert.assertNotNull(todaySchedule);
    }

    @Test
    public void testEngineerCantHaveShiftsOnConsecutiveDates() throws IOException {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, -1);

        DaySchedule schedule = new DaySchedule(new ObjectId().toString(), engineer1.getId(),
                engineer2.getId(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
        scheduleIRepository.add(schedule);

        IScheduleService service = new ScheduleServiceImpl();

        DaySchedule todaySchedule = service.getDayScheduleFor(today);
        Assert.assertNotEquals(engineer1.getId(), todaySchedule.getFirstHalfEngineerId());
        Assert.assertNotEquals(engineer1.getId(), todaySchedule.getSecondHalfEngineerId());
        Assert.assertNotEquals(engineer2.getId(), todaySchedule.getFirstHalfEngineerId());
        Assert.assertNotEquals(engineer2.getId(), todaySchedule.getSecondHalfEngineerId());

        scheduleIRepository.remove(todaySchedule);
        Engineer engineer4 = new Engineer("Enginner4", "Engineer4");
        engineerIRepository.add(engineer4);
        todaySchedule = service.getDayScheduleFor(today);
        Assert.assertNotEquals(engineer1.getId(), todaySchedule.getFirstHalfEngineerId());
        Assert.assertNotEquals(engineer1.getId(), todaySchedule.getSecondHalfEngineerId());
        Assert.assertNotEquals(engineer2.getId(), todaySchedule.getFirstHalfEngineerId());
        Assert.assertNotEquals(engineer2.getId(), todaySchedule.getSecondHalfEngineerId());
    }

    @Test
    public void testEnginearCanDoAtmostOneHalfDayShiftInADay() throws IOException {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, -1);

        DaySchedule schedule = new DaySchedule(new ObjectId().toString(), engineer1.getId(),
                engineer2.getId(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
        scheduleIRepository.add(schedule);

        IScheduleService service = new ScheduleServiceImpl();

        DaySchedule todaySchedule = service.getDayScheduleFor(today);
        Assert.assertNotEquals(todaySchedule.getFirstHalfEngineerId(), todaySchedule.getSecondHalfEngineerId());
    }

    @Before
    public void cleanUp() throws IOException {
        MongoCollection<Document> scheduleCollection = database.getCollection(DayScheduleMongoRepository.COLLECTION_NAME);
        scheduleCollection.deleteMany(new Document());
        MongoCollection<Document> engineerCollection = database.getCollection(EngineerMongoRepository.COLLECTION_NAME);
        engineerCollection.deleteMany(new Document());

        engineer1 = new Engineer("Engineer1", "Engineer1");
        engineer2 = new Engineer("Engineer2", "Engineer2");
        engineer3 = new Engineer("Engineer3", "Engineer3");

        List<Engineer> engineerList = new ArrayList<Engineer>();
        engineerList.add(engineer1);
        engineerList.add(engineer2);
        engineerList.add(engineer3);
        engineerIRepository.addAll(engineerList);
    }

}
