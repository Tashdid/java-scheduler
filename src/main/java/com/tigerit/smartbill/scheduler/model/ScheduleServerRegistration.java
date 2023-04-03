package com.tigerit.smartbill.scheduler.model;

import com.tigerit.smartbill.scheduler.service.monitoring.ServerRegistrationScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Component;

@Component
public class ScheduleServerRegistration implements ISchedulerInterface {

	@Autowired
	ServerRegistrationScheduler serverRegistrationScheduler;
	
	@Override
	public Runnable getRunnable() {
		return () -> serverRegistrationScheduler.executeScheduler();
	}
	
    @Override
	public Trigger getTrigger() {
		return triggerContext -> serverRegistrationScheduler.nextTryCron();
	}
}