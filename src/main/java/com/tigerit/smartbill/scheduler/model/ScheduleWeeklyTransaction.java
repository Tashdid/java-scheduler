package com.tigerit.smartbill.scheduler.model;

import com.tigerit.smartbill.scheduler.config.SchedulerConfigValues;
import com.tigerit.smartbill.scheduler.service.monitoring.WeeklyTransactionScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class ScheduleWeeklyTransaction implements ISchedulerInterface {

    SchedulerConfigValues schedulerConfigValues;
    WeeklyTransactionScheduler weeklyTransactionScheduler;

    private boolean isDataSaved = false;

    @Qualifier("inMemorySchedulerConfigValues")
    @Autowired
    public void setSchedulerConfigValues(SchedulerConfigValues schedulerConfigValues) {
        this.schedulerConfigValues = schedulerConfigValues;
    }

    @Autowired
    public void setWeeklyTransactionScheduler(WeeklyTransactionScheduler weeklyTransactionScheduler) {
        this.weeklyTransactionScheduler = weeklyTransactionScheduler;
    }

    @Override
    public Runnable getRunnable() {
        return () -> isDataSaved = weeklyTransactionScheduler.RunnableWeeklyTransaction();
    }

    @Override
    public Trigger getTrigger() {
        return triggerContext -> {
            String cron = null;
            if (!isDataSaved) {
                /** set same day different interval task */
                cron = schedulerConfigValues.getWeeklyCronJobIfFailed();
            } else {
                cron = schedulerConfigValues.getConfigWeeklyCron();
            }
            log.info(cron);
            
            long nextExecTime = Long.parseLong(cron);
            PeriodicTrigger periodicTrigger = new PeriodicTrigger(nextExecTime);
            Date nextExec = periodicTrigger.nextExecutionTime(triggerContext);
            return nextExec;
        };
    }
}