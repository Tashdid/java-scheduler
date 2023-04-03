package com.tigerit.smartbill.scheduler.service.monitoring;

import com.tigerit.smartbill.common.util.DynamicGlobals;
import com.tigerit.smartbill.common.util.Utils;
import com.tigerit.smartbill.scheduler.config.props.CustomAppProperties;
import com.tigerit.smartbill.scheduler.service.network.TigerITBillingServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.tigerit.smartbill.common.values.APIValues.BillingServer.Scheduler.GET_PDF_BILLS_TO_BE_CREATED;
import static com.tigerit.smartbill.common.values.APIValues.BillingServer.Scheduler.SCHEDULER_CONTROLLER_BASE_PATH;

@Component
@Slf4j
public class PayableBillingRecordScheduler {


    @Autowired
    CustomAppProperties customAppProperties;

    public void generatePDFScheduler() {
        //Setting up today value
        if (DynamicGlobals.useActualTodayValue) {
            String actualTodayValue = ""; //eg: format: 3/5/2019
            Date date = new Date();
            actualTodayValue = Utils.getDayStringFromJavaDate(date);
            DynamicGlobals.todayString = actualTodayValue;
        }

        if (DynamicGlobals.todayString.isEmpty()) {
            log.error("Date value is empty!!!");
            log.info("exiting thread");
            return;
        }

        String pdfBillsCreatedEndpoint = SCHEDULER_CONTROLLER_BASE_PATH
                + GET_PDF_BILLS_TO_BE_CREATED
                + "?today=" + DynamicGlobals.todayString; // fix todayString value , check format ??

        TigerITBillingServer tigerITBillingServer = new TigerITBillingServer();
        Object payload = tigerITBillingServer.postData(pdfBillsCreatedEndpoint, null);
/*
        String billingServerBaseUrl = customAppProperties.getBillingServerWithPort(); //"192.168.106.213:10081/billing"; //eg: "localhost:8081"
        billingServerBaseUrl += APIValues.BillingServer.CONTEXT_PATH;

        String billingUsername = customAppProperties.getBillingUsername(); //"test"
        String billingPassword = customAppProperties.getBillingPassword(); //"test123"

        String fullPOST_Url = "http://"
                + billingServerBaseUrl
                + SCHEDULER_CONTROLLER_BASE_PATH
                + GET_PDF_BILLS_TO_BE_CREATED
                + "?today=" + DynamicGlobals.todayString; // fix todayString value

        RestTemplate restTemplate = new RestTemplate ();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(billingUsername, billingPassword));

        Object payload = null;
        
        try {
        	ResponseEntity<GenericAPIResponse> response = restTemplate.exchange(fullPOST_Url, HttpMethod.POST, null, GenericAPIResponse.class);
            //restTemplate.postForObject (fullPOST_Url, null, GenericAPIResponse.class);
        	payload = ResponseConverter.convertResponseToPayload(response);
        }
        catch (Exception ex) {
        	log.error (ex.getMessage());
        }
*/
        if (payload != null) {
        }
    }

}
