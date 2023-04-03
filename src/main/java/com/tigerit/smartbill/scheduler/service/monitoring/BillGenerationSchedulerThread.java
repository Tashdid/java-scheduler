//#region <DevTJ>
package com.tigerit.smartbill.scheduler.service.monitoring;

import com.tigerit.smartbill.common.util.Utils;
import com.tigerit.smartbill.scheduler.config.props.CustomAppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@EnableScheduling
@Component
public class BillGenerationSchedulerThread {
    //#region [Dynamic Configuration]
    @Autowired
    CustomAppProperties customAppProperties;
    //#endregion [Dynamic Configuration]

    //@Value(value = "${producer_server_api_user_name}")
    //String producerServerApiUserName;

    //@Value(value = "${producer_server_api_user_password}")
    //String producerServerApiUserPassword;

    //@Scheduled(fixedRate = 5000, initialDelay = 5000) //todo: configure through external file
    public void executeScheduler() {
        //create thread pool and submit task
        log.info("Creating Executor Service\n");

        //region [DevAR]
        Integer totelNumberOfThread = Integer.parseInt(customAppProperties.getBatchThreadNo()); //1; //5;
        if (totelNumberOfThread == 0) totelNumberOfThread = 1;
        
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(totelNumberOfThread);

        for (int ii=0; ii<totelNumberOfThread; ii++) {
            log.info(Utils.prepareLogMessage("Creating Executor Service Task\n"));
            BillGeneratorTask task = new BillGeneratorTask(customAppProperties, ii);

            log.info(Utils.prepareLogMessage("Submitting task to executor"));
            executorService.submit(task);
        }

        log.info(Utils.prepareLogMessage("Active thread: " + executorService.getActiveCount()));
        while (executorService.getActiveCount() > 0) {
            long completeTaskCount = executorService.getCompletedTaskCount();
            //log.info(Utils.prepareLogMessage("Complete Task thread: " + completeTaskCount));
            if (completeTaskCount == totelNumberOfThread) break;
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {

            }
        }
        log.info(Utils.prepareLogMessage("Complete all Task thread: "));

        //endregion [DevAR]
    }
}
//#endregion <DevTJ>
