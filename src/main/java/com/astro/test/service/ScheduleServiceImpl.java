package com.astro.test.service;

import com.astro.test.db.IRepository;
import com.astro.test.db.dto.DaySchedule;
import com.astro.test.db.dto.Engineer;
import com.astro.test.db.mongo.repository.DayScheduleMongoRepository;
import com.astro.test.db.mongo.repository.EngineerMongoRepository;
import com.astro.test.db.mongo.specification.*;
import com.astro.test.util.ConfigurationReader;
import com.astro.test.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.*;

/**
 * Created by sahanlm on 3/25/18.
 */
public class ScheduleServiceImpl implements IScheduleService  {

    private static final Logger logger = LogManager.getLogger(ScheduleServiceImpl.class);

    private IRepository<Engineer> engineerIRepository;
    private IRepository<DaySchedule> scheduleIRepository;
    private ConfigurationReader configurationReader;

    public ScheduleServiceImpl() throws IOException {
        this.engineerIRepository = new EngineerMongoRepository();;
        this.scheduleIRepository = new DayScheduleMongoRepository();

        this.configurationReader = ConfigurationReader.getInstance();
    }

    public DaySchedule getDayScheduleFor(Date day) {
        List<DaySchedule> scheduleList = this.scheduleIRepository.query(new FindDayScheduleForDateSpecification(day));

        if (scheduleList.size() > 0) {
            return scheduleList.get(0);
        }

        List<Engineer> engineerList = engineerIRepository.query(new FindAllEngineersSpecification());

        Boolean skipConsecutiveWorkingDays = Boolean.parseBoolean(this.configurationReader.getConfig(Constants.SKIP_CONSECUTIVE_WORKING_DAYS));
        Boolean notAllowedTwoHalfDayShiftsInSameDay = Boolean.parseBoolean(this.configurationReader.getConfig(Constants.NOT_ALLOWED_TWO_HALF_DAY_SHIFT_IN_SAME_DAY));
        Integer numberOfShiftsAnEngineerCanHaveForAFortNight = Integer.parseInt(this.configurationReader.getConfig(Constants.NUM_OF_SHIFT_FOR_A_FORTNIGHT));

        List<Integer> skipList = new ArrayList<Integer>();
        if(skipConsecutiveWorkingDays) {
            this.updateSkipListForConsecutiveWorkingDays(day, engineerList, skipList);
        }

        this.imposeNumberOfShiftsRule(skipList, engineerList, numberOfShiftsAnEngineerCanHaveForAFortNight, day);

        int firstHalf = this.getRandomEngineerIndex(skipList, engineerList.size());
        if (notAllowedTwoHalfDayShiftsInSameDay == true) {
            skipList.add(firstHalf);
        }
        int secondHalf = this.getRandomEngineerIndex(skipList, engineerList.size());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);

        DaySchedule schedule = new DaySchedule();
        schedule.setId(new ObjectId().toString());
        if (firstHalf != -1) schedule.setFirstHalfEngineerId(engineerList.get(firstHalf).getId());
        if (secondHalf != -1) schedule.setSecondHalfEngineerId(engineerList.get(secondHalf).getId());
        schedule.setYear(calendar.get(Calendar.YEAR));
        schedule.setMonth(calendar.get(Calendar.MONTH) + 1);
        schedule.setDate(calendar.get(Calendar.DATE));

        String _id = scheduleIRepository.add(schedule);
        return schedule;
    }

    public List<Engineer> getEngineerScheduledFor(Date day) {
        DaySchedule daySchedule = this.getDayScheduleFor(day);

        List<String> idList = new ArrayList<String>();
        idList.add(daySchedule.getFirstHalfEngineerId());
        idList.add(daySchedule.getSecondHalfEngineerId());

        List<Engineer> engineerList = engineerIRepository.query(new FindEngineersByIdListSpecification(idList));

        return engineerList;
    }

    public List<DaySchedule> listAllSchedules() {
        return scheduleIRepository.query(new FindAllDayScheduleSpecification());
    }
    
    private int getRandomEngineerIndex(List<Integer> skipList, int n) {
        Random random = new Random();
        int result = -1;
        if (skipList.size() == n) {
            return result;
        }
        while (result == -1) {
            int randInt = random.nextInt(n);
            boolean stop = true;
            for (int c: skipList) {
                if (c == randInt) {
                    stop = false;
                }
            }
            if (stop) {
                result = randInt;
            }
        }
        return result;
    }

    private void updateSkipListForConsecutiveWorkingDays(Date day, List<Engineer> engineerList, List<Integer> skipList) {
        Calendar yesterDay = Calendar.getInstance();
        yesterDay.setTime(day);
        yesterDay.add(Calendar.DATE, -1);

        List<DaySchedule> yesterdayScheduleList = this.scheduleIRepository.query(new FindDayScheduleForDateSpecification(yesterDay.getTime()));
        if (yesterdayScheduleList.size() == 0) {
            return;
        }
        DaySchedule yesterDaySchedule = yesterdayScheduleList.get(0);

        for (int i = 0; i<engineerList.size(); i++) {
            Engineer e = engineerList.get(i);
            if (e.getId().compareTo(yesterDaySchedule.getFirstHalfEngineerId()) == 0 ||
                    e.getId().compareTo(yesterDaySchedule.getSecondHalfEngineerId()) == 0) {
                skipList.add(i);
            }
        }
    }

    private void imposeNumberOfShiftsRule(List<Integer> skipList, List<Engineer> engineerList,
                                          int allowedNumOfShiftPerFortnight, Date date) {
        Calendar to = Calendar.getInstance();
        to.setTime(date);

        Calendar from = Calendar.getInstance();
        from.setTime(date);
        from.add(Calendar.DATE, -10);

        for (int i=0; i<engineerList.size(); i++) {
            if (skipList.indexOf(i) == -1) {
                List<DaySchedule> engSchedule = this.scheduleIRepository.query(
                        new FindSchedulesBetweenTwoDaysForUserSpecification(from.getTime(), to.getTime(),
                                engineerList.get(i).getId()));
                if (engSchedule.size() >= allowedNumOfShiftPerFortnight) {
                    skipList.add(i);
                }
            }
        }
    }
    
}
