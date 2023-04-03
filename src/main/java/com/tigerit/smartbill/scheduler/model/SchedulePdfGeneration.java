package com.tigerit.smartbill.scheduler.model;

import com.tigerit.smartbill.common.util.DateConvertUtils;
import com.tigerit.smartbill.scheduler.config.SchedulerConfigValues;
import com.tigerit.smartbill.scheduler.service.monitoring.PayableBillingRecordScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class SchedulePdfGeneration implements ISchedulerInterface {
	
	SchedulerConfigValues schedulerConfigValues;

    @Qualifier("inMemorySchedulerConfigValues")
    @Autowired
    public void setSchedulerConfigValues(SchedulerConfigValues schedulerConfigValues) {
        this.schedulerConfigValues = schedulerConfigValues;
    }

    @Autowired
    PayableBillingRecordScheduler payableBillingRecordScheduler;
    
	@Override
	public Runnable getRunnable() {
		return () -> payableBillingRecordScheduler.generatePDFScheduler();
	}
	
    @Override
	public Trigger getTrigger() {
		return triggerContext -> {
            String cron = schedulerConfigValues.getPdfGenerationCron();
            log.info(cron);

            CronTrigger trigger = new CronTrigger(cron);
            Date nextExec = trigger.nextExecutionTime(triggerContext);

            String dateString = DateConvertUtils.jDate_to_TimeStampString (nextExec);
            log.info("[SCHEDULER] PDF-GENERATION SCHEDULER INITIATING .......... next call " + dateString);

            return nextExec;
        };
	}
}
