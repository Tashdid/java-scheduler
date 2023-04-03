package com.tigerit.smartbill.scheduler.model;

import com.tigerit.smartbill.scheduler.config.SchedulerConfigValues;
import com.tigerit.smartbill.scheduler.service.monitoring.DataArchiveScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class ScheduleDataBackup implements ISchedulerInterface {

    SchedulerConfigValues schedulerConfigValues;
    DataArchiveScheduler dataArchiveScheduler;

    @Autowired
    public void setDataArchiveScheduler(DataArchiveScheduler dataArchiveScheduler) {
        this.dataArchiveScheduler = dataArchiveScheduler;
    }

    @Qualifier("inMemorySchedulerConfigValues")
    @Autowired
    public void setSchedulerConfigValues(SchedulerConfigValues schedulerConfigValues) {
        this.schedulerConfigValues = schedulerConfigValues;
    }

    @Override
    public Runnable getRunnable() {
        return () -> dataArchiveScheduler.initDataArchiveScheduler();
    }

    @Override
    public Trigger getTrigger() {
        return triggerContext -> {
            String cron = schedulerConfigValues.getDataArchiveCron();
            log.info(cron);

            long nextExecTime = Long.parseLong(cron);
            PeriodicTrigger periodicTrigger = new PeriodicTrigger(nextExecTime);
            Date nextExec = periodicTrigger.nextExecutionTime(triggerContext);
            return nextExec;
        };
    }
}