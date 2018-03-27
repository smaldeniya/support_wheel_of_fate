package com.astro.prod.web.rest;

import com.astro.test.api.lambda.pojo.GetAllSchedulesResponse;
import com.astro.test.api.lambda.pojo.GetDayScheduleRequest;
import com.astro.test.api.lambda.pojo.GetDayScheduleResponse;
import com.astro.test.db.dto.DaySchedule;
import com.astro.test.db.dto.Engineer;
import com.astro.test.service.IScheduleService;
import com.astro.test.service.ScheduleServiceImpl;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * Created by sahanlm on 3/27/18.
 */
@Path("/api/v1/schedule")
public class DayScheduleApi {

    private static final Logger logger = LogManager.getLogger(DayScheduleApi.class);
    private IScheduleService scheduleService;

    public DayScheduleApi() throws IOException {
        scheduleService = new ScheduleServiceImpl();
    }

    @POST
    @Path("/getSchedule")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GetDayScheduleResponse getSchedule(GetDayScheduleRequest request) {
        DaySchedule schedule = scheduleService.getDayScheduleFor(request.getDate());
        List<Engineer> engineerList = scheduleService.getEngineerScheduledFor(request.getDate());

        GetDayScheduleResponse response = new GetDayScheduleResponse();
        response.setDate(request.getDate());
        for (Engineer e: engineerList) {
            if (e.getId().compareTo(schedule.getFirstHalfEngineerId()) == 0) {
                response.setFirstHalfDoneBy(e);
            }
            if (e.getId().compareTo(schedule.getSecondHalfEngineerId()) == 0) {
                response.setSecondHalfDoneBy(e);
            }
        }

        return response;
    }

    @GET
    @Path("/getAllSchedules")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GetAllSchedulesResponse getAllSchedules() {
        List<DaySchedule> scheduleList = scheduleService.listAllSchedules();
        GetAllSchedulesResponse response = new GetAllSchedulesResponse();
        response.setDaySchedules(scheduleList);
        return response;
    }

}
