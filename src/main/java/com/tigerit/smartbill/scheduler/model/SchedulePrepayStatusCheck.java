package com.tigerit.smartbill.scheduler.model;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.stereotype.Component;

import java.util.Date;

//import com.tigerit.smartbill.common.util.DateConvertUtils;

//import lombok.extern.slf4j.Slf4j;

//@Slf4j
@Component
public class SchedulePrepayStatusCheck implements ISchedulerInterface {
	
	@Override
	public Runnable getRunnable() {
		return new Runnable () {
            @Override
            public void run() {            	
            }
        };
	}
	
    @Override
	public Trigger getTrigger() {
		return new Trigger () {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                return null;
            }
        };
	}
}