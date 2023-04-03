//package com.tigerit.smartbill.scheduler.service;
//
////import com.tigerit.smartbill.common.dto.SchedulerDTO;
////import com.tigerit.smartbill.common.model.api.response.common.GenericAPIResponse;
////import com.tigerit.smartbill.common.util.ResponseConverter;
////import com.tigerit.smartbill.common.values.APIValues;
//import com.tigerit.smartbill.scheduler.config.props.CustomAppProperties;
////import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.HttpEntity;
////import org.springframework.http.HttpMethod;
////import org.springframework.http.ResponseEntity;
////import org.springframework.http.client.support.BasicAuthenticationInterceptor;
//import org.springframework.stereotype.Component;
////import org.springframework.web.client.RestTemplate;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ScheduledFuture;
//
////import static com.tigerit.smartbill.common.values.APIValues.BillingServer.Scheduler.GET_SCHEDULER_LIST;
////import static com.tigerit.smartbill.common.values.APIValues.BillingServer.Scheduler.SCHEDULER_CONTROLLER_BASE_PATH;
//import static com.tigerit.smartbill.scheduler.service.monitoring.SchedulerConfiguration.*;
//
//@Component
////@Slf4j
//public class BillingServerRemoteAPI {
//
//    //private static final String CONST_GET = "GET";
//    //private static final String CONST_POST = "POST";
//    
//    private CustomAppProperties customAppProperties;
//
//    @Autowired
//    public void setCustomAppProperties(CustomAppProperties customAppProperties) {
//        this.customAppProperties = customAppProperties;
//    }
//
////    private Object initRemoteAPI(String restAPIURL, String requestParam,
////                                 SchedulerDTO schedulerDTO,
////                                 String requestType) {
////
////        //"192.168.106.213:10081/billing"; //eg: "localhost:8081"
////        String billingServerBaseUrl = customAppProperties.getBillingServerWithPort();
////        billingServerBaseUrl += APIValues.BillingServer.CONTEXT_PATH;
////
////        String billingUsername = customAppProperties.getBillingUsername();
////        String billingPassword = customAppProperties.getBillingPassword();
////
////        String fullGET_Url = "http://" + billingServerBaseUrl
////                + SCHEDULER_CONTROLLER_BASE_PATH
////                + restAPIURL;
////
////        Object customersPayload = null;
////
////        RestTemplate accountsRestTemplate = new RestTemplate();
////        accountsRestTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(billingUsername, billingPassword));
////
////        if (requestType.equals(CONST_GET)) {
////            try {
////                ResponseEntity<GenericAPIResponse> customersResponse =
////                        accountsRestTemplate.exchange(
////                                fullGET_Url, HttpMethod.GET, null, GenericAPIResponse.class, requestParam);
////                customersPayload = ResponseConverter.convertResponseToPayload(customersResponse);
////            } catch (Exception ex) {
////                log.error(ex.getMessage());
////                return null;
////            }
//////            return customersPayload;
////        } else if (requestType.equals(CONST_POST)) {
////            try {
////                HttpEntity<SchedulerDTO> httpEntity = new HttpEntity<>(schedulerDTO);
////                ResponseEntity<GenericAPIResponse> customersResponse =
////                        accountsRestTemplate.exchange(
////                                fullGET_Url, HttpMethod.POST, httpEntity, GenericAPIResponse.class);
////                customersPayload = ResponseConverter.convertResponseToPayload(customersResponse);
////            } catch (Exception ex) {
////                log.error(ex.getMessage());
////                return null;
////            }
//////            return customersPayload;
////        }
////        return customersPayload;
////
////    }
////
////    public Map<String, ScheduledFuture<?>> GetSchedulerList() {
////        Map<String, ScheduledFuture<?>> futureMap =
////                (Map<String, ScheduledFuture<?>>) initRemoteAPI(
////                        GET_SCHEDULER_LIST, null, null, CONST_GET);
////
////        return futureMap;
////    }
//
////    public Map<String, ScheduledFuture<?>> registerServer() {
////    	Map<String, ScheduledFuture<?>> futureMap = new HashMap<>();
////    	
////    	if (customAppProperties.getBillGeneration().compareTo(SCHEDULER_DISABLE) != 0) futureMap.put(SCHEDULER_BILL_GENERATION, null);
////    	if (customAppProperties.getPrepayStatusCheck().compareTo(SCHEDULER_DISABLE) != 0) futureMap.put(SCHEDULER_PREPAY_STATUS_CHECK, null);
////    	if (customAppProperties.getPdfGeneration().compareTo(SCHEDULER_DISABLE) != 0) futureMap.put(SCHEDULER_PDF_GENERATION, null);
////    	if (customAppProperties.getEmailSmsNotification().compareTo(SCHEDULER_DISABLE) != 0) futureMap.put(SCHEDULER_EMAIL_SMS_NOTIFICATION, null);
////    	if (customAppProperties.getDailyTransaction().compareTo(SCHEDULER_DISABLE) != 0) futureMap.put(SCHEDULER_DAILY_TRANSACTION, null);
////    	if (customAppProperties.getWeeklyTransaction().compareTo(SCHEDULER_DISABLE) != 0) futureMap.put(SCHEDULER_WEEKLY_TRANSACTION, null);
////    	if (customAppProperties.getMonthlyTransaction().compareTo(SCHEDULER_DISABLE) != 0) futureMap.put(SCHEDULER_MONTHLY_TRANSACTION, null);
////    	if (customAppProperties.getDataArchive().compareTo(SCHEDULER_DISABLE) != 0) futureMap.put(SCHEDULER_DATA_ARCHIVE, null);
////
////    	return futureMap;
////    }
////    
////    public void unregisterServer() {
////    }
//}
