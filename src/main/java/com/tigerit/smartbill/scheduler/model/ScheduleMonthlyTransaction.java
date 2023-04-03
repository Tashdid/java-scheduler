package com.tigerit.smartbill.scheduler.model;

import com.tigerit.smartbill.scheduler.config.SchedulerConfigValues;
import com.tigerit.smartbill.scheduler.service.monitoring.MonthlyTransactionScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class ScheduleMonthlyTransaction implements ISchedulerInterface {

    SchedulerConfigValues schedulerConfigValues;
    MonthlyTransactionScheduler monthlyTransactionScheduler;

    private boolean isDataSaved = false;

    @Qualifier("inMemorySchedulerConfigValues")
    @Autowired
    public void setSchedulerConfigValues(SchedulerConfigValues schedulerConfigValues) {
        this.schedulerConfigValues = schedulerConfigValues;
    }

    @Autowired
    public void setMonthlyTransactionScheduler(MonthlyTransactionScheduler monthlyTransactionScheduler) {
        this.monthlyTransactionScheduler = monthlyTransactionScheduler;
    }

    @Override
    public Runnable getRunnable() {
        return () -> isDataSaved = monthlyTransactionScheduler.RunnableMonthlyTransaction();
    }

    @Override
    public Trigger getTrigger() {
        return triggerContext -> {
            String cron = null;
            if (!isDataSaved) {
                /** set same day different interval task */
                cron = schedulerConfigValues.getMonthlyCronJobIfFailed();
            } else {
                cron = schedulerConfigValues.getConfigMonthlyCron();
            }
            log.info(cron);
            
            long nextExecTime = Long.parseLong(cron);
            PeriodicTrigger periodicTrigger = new PeriodicTrigger(nextExecTime);
            Date nextExec = periodicTrigger.nextExecutionTime(triggerContext);
            return nextExec;
        };
    }
}