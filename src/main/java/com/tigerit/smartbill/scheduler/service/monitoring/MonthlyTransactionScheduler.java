package com.tigerit.smartbill.scheduler.service.monitoring;

import com.tigerit.smartbill.common.values.APIValues;
import com.tigerit.smartbill.scheduler.service.network.TigerITBillingServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.tigerit.smartbill.common.values.APIValues.BillingServer.Scheduler.GENERATE_REPORT_MONTHLY;

@Slf4j
@Component
public class MonthlyTransactionScheduler {
	
	public boolean RunnableMonthlyTransaction() {
        log.info ("Entering Monthly Report generation task , Start Time " + LocalDate.now ());

        Object payload = null;
        
        String fullGET_URL = APIValues.BillingServer.Scheduler.SCHEDULER_CONTROLLER_BASE_PATH + GENERATE_REPORT_MONTHLY;
        
        try {
        	TigerITBillingServer tigerITBillingServer = new TigerITBillingServer();
        	payload = tigerITBillingServer.getForObject(fullGET_URL);
        	if (payload != null) payload=true;
        } catch (Exception ex) {
            /** data logged didn't succeed , need to config scheduler time */
            payload=false;
            log.info ("[Monthly Report Generation Error] " + ex.getMessage ());
        }

		return (boolean) payload;
	}
}