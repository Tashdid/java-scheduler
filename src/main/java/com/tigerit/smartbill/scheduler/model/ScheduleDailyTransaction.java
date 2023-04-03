package com.tigerit.smartbill.scheduler.model;

import com.tigerit.smartbill.scheduler.config.SchedulerConfigValues;
import com.tigerit.smartbill.scheduler.service.monitoring.DailyTransactionScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class ScheduleDailyTransaction implements ISchedulerInterface {

    SchedulerConfigValues schedulerConfigValues;

    @Autowired
    DailyTransactionScheduler dailyTransactionScheduler;

    private boolean isDataSaved = false;

    @Qualifier("inMemorySchedulerConfigValues")
    @Autowired
    public void setSchedulerConfigValues(SchedulerConfigValues schedulerConfigValues) {
        this.schedulerConfigValues = schedulerConfigValues;
    }

    @Override
    public Runnable getRunnable() {
        return () -> isDataSaved = dailyTransactionScheduler.RunnableDailyTransaction();
    }

    @Override
    public Trigger getTrigger() {
        return triggerContext -> {
            String cron = null;
            if (!isDataSaved) {
                /** set same day different interval task */
                cron = schedulerConfigValues.getDailyCronJobIfFailed();
            } else {
                cron = schedulerConfigValues.getConfigDailyCron();
            }
            log.info(cron);

            long nextExecTime = Long.parseLong(cron);
            PeriodicTrigger periodicTrigger = new PeriodicTrigger(nextExecTime);
            Date nextExec = periodicTrigger.nextExecutionTime(triggerContext);
            return nextExec;
        };
    }
}