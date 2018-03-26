package com.astro.test.db.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by sahanlm on 3/23/18.
 */
public class DaySchedule implements Serializable{

    private String id;
    private String firstHalfEngineerId;
    private String secondHalfEngineerId;
    private int year;
    private int month;
    private int date;


    public DaySchedule() {}

    public  DaySchedule(String id, String firstHalfEngineerId, String secondHalfEngineerId,
                        int year, int month, int date) {
        this.id = id;
        this.firstHalfEngineerId = firstHalfEngineerId;
        this.secondHalfEngineerId = secondHalfEngineerId;
        this.year = year;
        this.month = month;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstHalfEngineerId() {
        return firstHalfEngineerId;
    }

    public void setFirstHalfEngineerId(String firstHalfEngineerId) {
        this.firstHalfEngineerId = firstHalfEngineerId;
    }

    public String getSecondHalfEngineerId() {
        return secondHalfEngineerId;
    }

    public void setSecondHalfEngineerId(String secondHalfEngineerId) {
        this.secondHalfEngineerId = secondHalfEngineerId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "1st half: " + this.firstHalfEngineerId + ", 2nd half " + this.secondHalfEngineerId + ", date:" +
                year + "/" + month + "/" + date;
    }
}
