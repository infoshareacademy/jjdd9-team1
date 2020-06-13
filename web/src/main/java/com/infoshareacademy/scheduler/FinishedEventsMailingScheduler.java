package com.infoshareacademy.scheduler;

import com.infoshareacademy.service.FinishedEventsService;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.logging.Logger;

@Singleton
@Startup
public class FinishedEventsMailingScheduler {

    private static final Logger STDLOG = Logger.getLogger(FinishedEventsMailingScheduler.class.getName());

    @Inject
    FinishedEventsService finishedEventsService;

    @Schedule(hour = "*", minute = "*", second = "0", info = "Each minute")
    public void fixedRate() {
        finishedEventsService.finishedManager();
        STDLOG.severe("\n\n\n\n ");


    }
}
