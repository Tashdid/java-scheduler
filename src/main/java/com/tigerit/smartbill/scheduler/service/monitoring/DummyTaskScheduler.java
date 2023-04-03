package com.tigerit.smartbill.scheduler.service.monitoring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DummyTaskScheduler {

    public void initFakeTask() {
        log.info("running fake task for testing purpose ::::----::::::----:::::--\n");

    }
}
