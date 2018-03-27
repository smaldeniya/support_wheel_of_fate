package com.astro.prod.web;

import com.astro.prod.web.rest.DayScheduleApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wso2.msf4j.MicroservicesRunner;

/**
 * Created by sahanlm on 3/27/18.
 */
public class RestServer {

    private static final Logger logger = LogManager.getLogger(RestServer.class);

    public static void main(String[] args) {
        try {
            new MicroservicesRunner(8082).deploy(new DayScheduleApi()).start();
        } catch (Exception e) {
            logger.error(e);
        }
    }

}
