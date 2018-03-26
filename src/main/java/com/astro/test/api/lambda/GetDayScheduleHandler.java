package com.astro.test.api.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.astro.test.api.lambda.pojo.GetDayScheduleRequest;
import com.astro.test.api.lambda.pojo.GetDayScheduleResponse;
import com.astro.test.db.dto.DaySchedule;
import com.astro.test.db.dto.Engineer;
import com.astro.test.db.mongo.repository.DayScheduleMongoRepository;
import com.astro.test.db.mongo.repository.EngineerMongoRepository;
import com.astro.test.db.mongo.specification.FindEngineersByIdSpecification;
import com.astro.test.service.IScheduleService;
import com.astro.test.service.ScheduleServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by sahanlm on 3/26/18.
 */
public class GetDayScheduleHandler implements RequestHandler<GetDayScheduleRequest, GetDayScheduleResponse> {

    private static final Logger logger = LogManager.getLogger(GetDayScheduleHandler.class);

    public GetDayScheduleResponse handleRequest(GetDayScheduleRequest getDayScheduleRequest, Context context) {
        try {

            IScheduleService scheduleService = new ScheduleServiceImpl();

            Date scheduleDate = getDayScheduleRequest.getDate();
            if (scheduleDate == null) {
                scheduleDate = new Date();
            }

            DaySchedule schedule = scheduleService.getDayScheduleFor(scheduleDate);
            List<Engineer> engineerList = scheduleService.getEngineerScheduledFor(scheduleDate);

            GetDayScheduleResponse response = new GetDayScheduleResponse();

            for (Engineer e: engineerList) {
                if (e.getId().compareTo(schedule.getFirstHalfEngineerId()) == 0) {
                    response.setFirstHalfDoneBy(e);
                }
                if (e.getId().compareTo(schedule.getSecondHalfEngineerId()) == 0) {
                    response.setSecondHalfDoneBy(e);
                }
            }

            response.setDate(scheduleDate);

            return response;
        } catch (IOException e) {
            logger.error(e);
            return null;
        }
    }
}
