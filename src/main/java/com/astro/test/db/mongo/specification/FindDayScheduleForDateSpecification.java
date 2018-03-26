package com.astro.test.db.mongo.specification;

import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by sahanlm on 3/25/18.
 */
public class FindDayScheduleForDateSpecification implements IMongoSpecification {

    private Date date;

    public FindDayScheduleForDateSpecification(Date date) {
        this.date = date;
    }

    public Bson toMongoQuery() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);
        Bson query = Filters.and(
                Filters.eq("year", calendar.get(Calendar.YEAR)),
                Filters.eq("month", calendar.get(Calendar.MONTH) + 1),
                Filters.eq("date", calendar.get(Calendar.DATE))
        );
        return query;
    }
}
