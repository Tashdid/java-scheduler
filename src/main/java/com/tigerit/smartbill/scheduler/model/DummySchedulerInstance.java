package com.tigerit.smartbill.scheduler.model;

import com.tigerit.smartbill.scheduler.config.SchedulerConfigValues;
import com.tigerit.smartbill.scheduler.service.monitoring.DummyTaskScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class DummySchedulerInstance implements ISchedulerInterface {

    SchedulerConfigValues schedulerConfigValues;
    DummyTaskScheduler dummyTaskScheduler;

    @Autowired
    public void setDummyTaskScheduler(DummyTaskScheduler dummyTaskScheduler) {
        this.dummyTaskScheduler = dummyTaskScheduler;
    }

    @Qualifier("inMemorySchedulerConfigValues")
    @Autowired
    public void setSchedulerConfigValues(SchedulerConfigValues schedulerConfigValues) {
        this.schedulerConfigValues = schedulerConfigValues;
    }

    @Override
    public Runnable getRunnable() {
        return () -> dummyTaskScheduler.initFakeTask();
    }

    @Override
    public Trigger getTrigger() {
        return triggerContext -> {
            String cron = schedulerConfigValues.getDummyTaskCron();
            log.info(cron);

            long nextExecTime = Long.parseLong(cron);
            PeriodicTrigger periodicTrigger = new PeriodicTrigger(nextExecTime);
            Date nextExec = periodicTrigger.nextExecutionTime(triggerContext);
            return nextExec;
        };
    }
}
