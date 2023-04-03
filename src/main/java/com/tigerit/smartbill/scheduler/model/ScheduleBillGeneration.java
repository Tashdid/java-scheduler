package com.tigerit.smartbill.scheduler.model;

import com.tigerit.smartbill.common.util.DateConvertUtils;
import com.tigerit.smartbill.scheduler.config.SchedulerConfigValues;
import com.tigerit.smartbill.scheduler.service.monitoring.BillGenerationSchedulerThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class ScheduleBillGeneration implements ISchedulerInterface {
	
    SchedulerConfigValues schedulerConfigValues;

    @Qualifier("inMemorySchedulerConfigValues")
    @Autowired
    public void setSchedulerConfigValues(SchedulerConfigValues schedulerConfigValues) {
        this.schedulerConfigValues = schedulerConfigValues;
    }

    @Autowired
    BillGenerationSchedulerThread billGenerationSchedulerThread;

    @Override
	public Runnable getRunnable() {
		return () -> {
            //billGenerationScheduler.executeScheduler();
            billGenerationSchedulerThread.executeScheduler ();
        };
	}
	
    @Override
	public Trigger getTrigger() {
		return triggerContext -> {

            /*
            Calendar nextExecutionTime = new GregorianCalendar ();
            Date lastActualExecutionTime = triggerContext.lastActualExecutionTime ();
            nextExecutionTime.setTime (lastActualExecutionTime != null ? lastActualExecutionTime : new Date ());
            int intTimeIntervalInMilliSeconds = (int) DynamicGlobals.timeIntervalInMilliSeconds;
            nextExecutionTime.add (Calendar.MILLISECOND, intTimeIntervalInMilliSeconds); //you can get the value from wherever you want

            Date date = nextExecutionTime.getTime ();
            String dateString = DateConvertUtils.jDate_to_TimeStampString (date);
            System.out.println ("[ATAUR]billGenerationSchedulerThread..........next call " + dateString);

            return nextExecutionTime.getTime ();
            */

            String cron = schedulerConfigValues.getBillGenerationCron();
            log.info(cron);

            CronTrigger trigger = new CronTrigger(cron);
            Date nextExec = trigger.nextExecutionTime(triggerContext);

            String dateString = DateConvertUtils.jDate_to_TimeStampString (nextExec);
            log.info("[ATAUR]billGenerationSchedulerThread..........next call " + dateString);

            return nextExec;
        };
	}
}
