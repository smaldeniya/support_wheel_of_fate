package com.astro.test.api.lambda.pojo;

import com.astro.test.db.dto.DaySchedule;

import java.util.List;

/**
 * Created by sahanlm on 3/27/18.
 */
public class GetAllSchedulesResponse {

    private List<DaySchedule> daySchedules;

    public List<DaySchedule> getDaySchedules() {
        return daySchedules;
    }

    public void setDaySchedules(List<DaySchedule> daySchedules) {
        this.daySchedules = daySchedules;
    }
}
