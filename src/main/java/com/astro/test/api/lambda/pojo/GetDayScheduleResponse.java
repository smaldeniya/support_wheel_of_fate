package com.astro.test.api.lambda.pojo;


import com.astro.test.db.dto.Engineer;

import java.util.Date;

/**
 * Created by sahanlm on 3/26/18.
 */
public class GetDayScheduleResponse {

    private Engineer firstHalfDoneBy;
    private Engineer secondHalfDoneBy;
    private Date date;

    public Engineer getFirstHalfDoneBy() {
        return firstHalfDoneBy;
    }

    public void setFirstHalfDoneBy(Engineer firstHalfDoneBy) {
        this.firstHalfDoneBy = firstHalfDoneBy;
    }

    public Engineer getSecondHalfDoneBy() {
        return secondHalfDoneBy;
    }

    public void setSecondHalfDoneBy(Engineer secondHalfDoneBy) {
        this.secondHalfDoneBy = secondHalfDoneBy;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
