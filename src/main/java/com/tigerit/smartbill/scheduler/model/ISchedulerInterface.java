package com.tigerit.smartbill.scheduler.model;

import org.springframework.scheduling.Trigger;

public interface ISchedulerInterface {
	public Runnable getRunnable();
	public Trigger getTrigger();
}
