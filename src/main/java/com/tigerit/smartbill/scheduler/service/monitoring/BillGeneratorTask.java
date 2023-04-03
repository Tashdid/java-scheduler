//#region <DevTJ>

package com.tigerit.smartbill.scheduler.service.monitoring;

import com.tigerit.smartbill.common.model.api.request.GetBatchBillingDataOnADateRequest;
import com.tigerit.smartbill.common.model.api.request.scheduler.GenerateBillingRecordRequest;
import com.tigerit.smartbill.common.model.api.response.GetBatchBillingDataOnADateResponse;
import com.tigerit.smartbill.common.model.api.response.common.GenericAPIResponse;
import com.tigerit.smartbill.common.model.api.response.scheduler.GenerateBillingRecordResponse;
import com.tigerit.smartbill.common.model.api.response.scheduler.GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse;
import com.tigerit.smartbill.common.model.dto.BillingAccountIdWithMeterId;
import com.tigerit.smartbill.common.util.DynamicGlobals;
import com.tigerit.smartbill.common.util.ResponseConverter;
import com.tigerit.smartbill.common.util.SmartMeterProjectUtils;
import com.tigerit.smartbill.common.util.Utils;
import com.tigerit.smartbill.scheduler.config.props.CustomAppProperties;
import com.tigerit.smartbill.scheduler.service.network.TigerITBillingServer;
import com.tigerit.smartbill.scheduler.service.network.TigerITProducerServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tigerit.smartbill.common.values.APIValues.BillingServer.Scheduler.*;
import static com.tigerit.smartbill.common.values.APIValues.ProducerServer.BillingDataProvider.BillingDataProvider_BASE_PATH;
import static com.tigerit.smartbill.common.values.APIValues.ProducerServer.BillingDataProvider.METERS_READING;

@Slf4j
public class BillGeneratorTask implements Runnable {

    String uniqueBatchId = "";
    String uniqueBatchSize = "";

    List<BillingAccountIdWithMeterId> billingAccountIdWithMeterIdList = new ArrayList<>();
    String dateString = null;
    boolean needToCleanBatchId = false;
    
    public BillGeneratorTask(CustomAppProperties customAppProperties, int threadNo) {
    	this.uniqueBatchId = SmartMeterProjectUtils.createBatchId(threadNo);
    	this.uniqueBatchSize = customAppProperties.getBatchFetchRow(); //"5";
//        if (uniqueBatchSize.isBlank()) uniqueBatchSize = "5";
        if (uniqueBatchSize.isEmpty()) uniqueBatchSize = "5";
    }

    @Override
    public void run() {
        log.info("Entering thread" ,Thread.currentThread().getName());

        if (uniqueBatchId.isEmpty()) {
        	log.error("Unique Batch Id is empty!!!");
        	log.info("exiting thread");
        	return;	
        }
        
        if (!needToCleanBatchId && billingAccountIdWithMeterIdList.isEmpty()) {        
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
	        
	        // Get a list of accounts of following date
	        String endpoint = SCHEDULER_CONTROLLER_BASE_PATH
	                + GET_ACCOUNTS_TO_BE_UPDATED
	                + "?" + REQUEST_PARAMETER__DAY + "=" + DynamicGlobals.todayString
	                + "&" + REQUEST_PARAMETER__BATCH_ID + "=" + uniqueBatchId
	                + "&" + REQUEST_PARAMETER__BATCH_SIZE + "=" + uniqueBatchSize;
	
	        Object payload = null;
	    	
	        try {
	        	TigerITBillingServer tigerITBillingServer = new TigerITBillingServer();
	        	payload = tigerITBillingServer.getData(endpoint) ;
	        }
	        catch (Exception ex) {
	        	log.error(ex.getMessage());
	        	log.info("exiting thread");
	        	
	        	return;
	        }
	        
	        if (payload == null) {
	        	log.error("Empty payload");
	        	log.info("exiting thread");
	        	
	        	return;
	        }
	        
	        GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse response = ResponseConverter
	        		.convertPayloadToCustomObject(payload, GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse.class);

	        if (response != null && response.getList() != null && response.getList().size() > 0) {
	        	response
	        		.getList()
	        			.forEach(billingAccountBillingDataDTO -> {
	        				BillingAccountIdWithMeterId billingAccountIdWithMeterId = new BillingAccountIdWithMeterId();

	        				billingAccountIdWithMeterId.setAccountNumber(billingAccountBillingDataDTO.getAccountNumber());
	        				billingAccountIdWithMeterId.setMeterDeviceId(billingAccountBillingDataDTO.getMeterId());
	        				
	        				billingAccountIdWithMeterIdList.add(billingAccountIdWithMeterId);
	        			});
	        	dateString = response.getDateString();
	        }

        }        
        
        if (!needToCleanBatchId && dateString != null) {
	        needToCleanBatchId = processTheseAccounts();
        }
        
        if (needToCleanBatchId) {
        	dateString = null;
        	billingAccountIdWithMeterIdList.clear();
        	
	        //todo: on error, set iteration flag as false
        	String endpoint = SCHEDULER_CONTROLLER_BASE_PATH
	                + CLEAR_MARKED_ACCOUNTS
	                + "?" + REQUEST_PARAMETER__BATCH_ID + "=" + uniqueBatchId;
        	
	        GenericAPIResponse restTemplateResponse = null;
	        
	        try {
	        	TigerITBillingServer tigerITBillingServer = new TigerITBillingServer();
	        	restTemplateResponse = tigerITBillingServer.getForObject(endpoint);
	        }
	        catch (Exception ex) {
	        	log.error(ex.getMessage());
	        }
	        
	        if (restTemplateResponse != null) {
	        	
	        }
        }
        
    	log.info("exiting thread");
    }

    private boolean processTheseAccounts() {
        if (billingAccountIdWithMeterIdList.size() == 0) {
        	log.warn("billingAccountIdWithMeterIdList.size() == 0");
        	return false;
        }
        
        GetBatchBillingDataOnADateRequest request = new GetBatchBillingDataOnADateRequest();
        request.setBillingAccountIdWithMeterIdList(billingAccountIdWithMeterIdList);
        request.setDateString(dateString);
        
        String endpoint = BillingDataProvider_BASE_PATH + METERS_READING;

        Object payload = null;
    	
        try {
        	TigerITProducerServer tigerITProducerServer = new TigerITProducerServer();
        	payload = tigerITProducerServer.postData(endpoint, new HttpEntity<>(request));
        }
        catch (Exception ex) {
        	log.error(ex.getMessage());
        	return false;
        }
        
        GetBatchBillingDataOnADateResponse producerResponse = ResponseConverter
        		.convertPayloadToCustomObject(payload, GetBatchBillingDataOnADateResponse.class);
        
        if (producerResponse != null && producerResponse.getDataList() != null) {
        	if (producerResponse.getDataList().size() != billingAccountIdWithMeterIdList.size()) {
        		if (producerResponse.getFailureAccountNumberList().size() != (billingAccountIdWithMeterIdList.size() - producerResponse.getDataList().size())) {
        			log.error("FailureAccountNumberList does not match with request and response!!!");
        		}
/*        
        		endpoint = SCHEDULER_CONTROLLER_BASE_PATH + CLEAR_MARKED_ACCOUNTS;
        		
        		GenericAPIResponse restTemplateResponse = null;
        		
    	        GetBillingAccountDataRequest restTemplateRequest = new GetBillingAccountDataRequest();
    	        restTemplateRequest.setBillingAccountList(producerResponse.getFailureAccountNumberList());
    	        restTemplateRequest.setDateString(dateString);
    	        
    	        try {
    	        	restTemplateResponse = TigerITBillingServer.postForObject(endpoint, restTemplateRequest);
    	        }
    	        catch (Exception ex) {
    	        	log.error(ex.getMessage());
    	        }
    	        
    	        if (restTemplateResponse == null || restTemplateResponse.getError() != null ) {    	        	
    	        }
*/    	        
        	}
        	
        	if (producerResponse.getDataList().size() == 0) return true;
        	else {
        		return processTheseBatchBillingData(producerResponse);
        	}
        }
        
        return false;
    }

    private boolean processTheseBatchBillingData(GetBatchBillingDataOnADateResponse response2) {
    	if (response2 == null || response2.getDataList() == null || response2.getDataList().size() == 0) {
    		log.warn("response2 == null || response2.getDataList() == null || response2.getDataList().size() == 0");
    		return false;
    	}

    	String dateString = response2.getDateString();
    	String endpoint = SCHEDULER_CONTROLLER_BASE_PATH + POST_NEW_BILLING_RECORDS_TO_BE_UPDATED;
    	
    	GenerateBillingRecordRequest request = new GenerateBillingRecordRequest();
    	request.setDayValue(dateString);
    	request.setDataList(response2.getDataList());
    	//ATAUR for test
    	//request.getDataList().get(0).setMeterId("POST-PAID");// for manual test purpose

    	//call billing server rest api
    	GenerateBillingRecordResponse generateBillingRecordResponse = null; //new GenerateBillingRecordResponse();
    	        
    	try {
    		TigerITBillingServer tigerITBillingServer = new TigerITBillingServer();
    		generateBillingRecordResponse = tigerITBillingServer.postForObject(endpoint, request, GenerateBillingRecordResponse.class);
    	}
    	catch (Exception ex) {
    		log.error(ex.getMessage());
    		return false;
        }
    	
    	if (generateBillingRecordResponse != null) {
    		//log.info(generateBillingRecordResponse.getStatus());
    		return true;
    	}
    	
    	return false;
    }
}
//#endregion <DevTJ>
