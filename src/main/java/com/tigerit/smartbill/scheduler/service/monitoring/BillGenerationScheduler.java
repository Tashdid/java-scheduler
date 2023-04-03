//package com.tigerit.smartbill.scheduler.service.monitoring;
//
//
//import com.tigerit.smartbill.common.model.api.request.GetBatchBillingDataOnADateRequest;
//import com.tigerit.smartbill.common.model.api.request.scheduler.GeneratePayableBillingRecordRequest;
//import com.tigerit.smartbill.common.model.api.response.GetBatchBillingDataOnADateResponse;
//import com.tigerit.smartbill.common.model.api.response.scheduler.GenerateBillingRecordResponse;
//import com.tigerit.smartbill.common.model.api.response.scheduler.GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse;
//import com.tigerit.smartbill.common.model.dto.BillingAccountIdWithMeterId;
//import com.tigerit.smartbill.common.property.exposer.ICustomPropertyResolutionService;
//import com.tigerit.smartbill.common.util.DynamicGlobals;
//import com.tigerit.smartbill.common.util.SmartMeterProjectUtils;
//import com.tigerit.smartbill.common.util.Utils;
//import com.tigerit.smartbill.common.values.APIValues;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.context.annotation.Bean;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import static com.tigerit.smartbill.common.util.Constants.LOG_MSG;
//import static com.tigerit.smartbill.common.values.APIValues.BillingServer.Scheduler.*;
//import static com.tigerit.smartbill.common.values.APIValues.ProducerServer.BillingDataProvider.BillingDataProvider_BASE_PATH;
//import static com.tigerit.smartbill.common.values.APIValues.ProducerServer.BillingDataProvider.METERS_READING;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//
//@Slf4j
//@EnableScheduling
//@Component
//public class BillGenerationScheduler {
//
//    //#region [Dynamic Configuration]
//    ICustomPropertyResolutionService customPropertyResolver;
//
//    @Autowired
//    public void setCustomPropertyResolver(ICustomPropertyResolutionService customPropertyResolver) {
//        this.customPropertyResolver = customPropertyResolver;
//    }
//    //#endregion [Dynamic Configuration]
//
//    //tmp
//        //todo: make it 'volatile'? for multi-threading's visibility issue
//    private volatile boolean isIterationRunning = false;
//
//    //BillGenerationServiceProvider billGenerationServiceProvider;
//
//    @Value(value = "${producer_server_api_user_name}")
//    String producerServerApiUserName;
//
//    @Value(value = "${producer_server_api_user_password}")
//    String producerServerApiUserPassword;
//
//    //TMP
//    //Map<String, String> inMemoryMap_METERID_vs_BillingAccountId = new HashMap<>();
//    //@Autowired
//    //public void setBillGenerationServiceProvider(BillGenerationServiceProvider billGenerationServiceProvider) {
//    //    this.billGenerationServiceProvider = billGenerationServiceProvider;
//    //}
//
//    //#region [Eureka]
//    //@Bean
//    //@LoadBalanced
//    //public WebClient.Builder loadBalancedWebClientBuilder() throws SSLException {
//    //    ////todo: trusting all certs only for DEV env
//    //    ////must change as per policy in PROD
//    //    //SslContext sslContext = SslContextBuilder
//    //    //        .forClient()
//    //    //        .trustManager(InsecureTrustManagerFactory.INSTANCE)
//    //    //        .build();
//    //    //ClientHttpConnector httpConnector =
//    //    //        (ClientHttpConnector)
//    //    //                HttpClient
//    //    //                        .create()
//    //    //                        .secure(t -> t.sslContext(sslContext));
//    //    //return WebClient.builder().clientConnector(httpConnector);//.build();
//    //
//    //
//    //    return WebClient.builder();
//    //}
//
//    //@Bean
//    //@LoadBalanced
//    //public WebClient createWebClient() throws SSLException {
//    //    SslContext sslContext = SslContextBuilder
//    //            .forClient()
//    //            .trustManager(InsecureTrustManagerFactory.INSTANCE)
//    //            .build();
//    //    var httpConnector = HttpClient.create().secure(t -> t.sslContext(sslContext) );
//    //    return WebClient
//    //            .builder()
//    //            .clientConnector(
//    //                    new ReactorClientHttpConnector(httpConnector)
//    //            )
//    //            .build();
//    //}
//    @Bean
//    @LoadBalanced
//    public WebClient.Builder loadBalancedWebClientBuilder() {
//        return WebClient.builder();
//    }
//
//    @Autowired
//    private WebClient.Builder webClientBuilder;
//
//    //@Autowired
//    //private WebClient webClient;
//
//    //#endregion [Eureka]
//
//    //#region [Simulation]
////    SchedulerSimulationConfigValues schedulerSimulationConfigValues;
////    @Qualifier("inMemorySchedulerSimulationConfigValues")
////    @Autowired
////    public void setSchedulerSimulationConfigValues(SchedulerSimulationConfigValues schedulerSimulationConfigValues) {
////        this.schedulerSimulationConfigValues = schedulerSimulationConfigValues;
////    }
////    SchedulerSimulationService.SimulationCallback simulationCallback;
////    public void setSimulationCallback(SchedulerSimulationService.SimulationCallback simulationCallback) {
////        this.simulationCallback = simulationCallback;
////    }
//    //#endregion [Simulation]
//
//    //@Scheduled(fixedRate = 5000, initialDelay = 5000) //todo: configure through external file
//    public void executeScheduler() {
//
//        ////region [DevMU]
//        //try {
//        //
//        //    LOG_MSG = "";
//        //    LOG_MSG += "Trying to go to sleep for 60s.";
//        //    log.error(Utils.prepareLogMessage(LOG_MSG));
//        //
//        //    Thread.sleep(60000); //this blocks the invocation of the infinite loop
//        //
//        //    LOG_MSG = "";
//        //    LOG_MSG += "60s sleep done.";
//        //    log.error(Utils.prepareLogMessage(LOG_MSG));
//        //
//        //} catch (Exception ex) {
//        //    LOG_MSG = "";
//        //    LOG_MSG += "Exception occurred while going to sleep or while sleeping";
//        //    log.error(Utils.prepareLogMessage(LOG_MSG), ex);
//        //}
//        ////endregion [DevMU]
//
//        if (!DynamicGlobals.shouldRunScheduler) {
//            if(isIterationRunning) {
//                isIterationRunning = false;
//
//                LOG_MSG = "One iteration was running when 'shouldRunScheduler == false' !!!. It has been turned off";
//                log.info(Utils.prepareLogMessage(LOG_MSG));
//            }
//            return;
//        }
//
//        //todo: uncomment this snippet when error handling is in place
//        if(isIterationRunning) {
//            LOG_MSG = "One iteration is running !!!";
//            log.info(Utils.prepareLogMessage(LOG_MSG));
//
//            if(DynamicGlobals.shouldRunScheduler == false) {
//                isIterationRunning = false;
//                LOG_MSG = "";
//                LOG_MSG += "Unexpected scenario: isIterationRunning == true. But, DynamicGlobals.shouldRunScheduler == false";
//                LOG_MSG += ". Therefore, setting and changing forcefully. >>> isIterationRunning = false";
//                log.info(Utils.prepareLogMessage(LOG_MSG));
//            }
//            return;
//        }
//
////        if(!schedulerSimulationConfigValues.isLastIterationSuccessful()) {
////            LOG_MSG = "!schedulerSimulationConfigValues.isLastIterationSuccessful().";
////            LOG_MSG += " Aborting/Skipping.";
////            log.warn(Utils.prepareLogMessage(LOG_MSG));
////            return;
////        }
//        //important:
////        schedulerSimulationConfigValues.setLastIterationSuccessful(false);
//        //important:
//        isIterationRunning = true;
//
//        //Setting up today value
//        if(DynamicGlobals.useActualTodayValue) {
//            String actualTodayValue = ""; //eg: format: 3/5/2019
//            Date date = new Date();
//            actualTodayValue = Utils.getDayStringFromJavaDate(date);
//            DynamicGlobals.todayString = actualTodayValue;
//        }
//
//        if (DynamicGlobals.todayString.isEmpty()) {
//        	log.error("Date value is empty!!!");
//        	log.info("exiting thread");
//        	return;	
//        }
//
//        //Coding manually, later may introduce another lower layer (eg: billGenerationServiceProvider)
//        //WebClient webClient = WebClient.create();
//        WebClient webClient = webClientBuilder.build(); //eureka
//        
//        String billingServerBaseUrl = customPropertyResolver.getBillingServerIpWithPort(); //eg: format: "localhost:8081"
//        //profile based
//        //billingServerBaseUrl = customPropertyResolver.getBillingServerIpWithPort() + "/billing"; //direct
//        //billingServerBaseUrl = "192.168.106.213:10081/billing"; //direct
//        billingServerBaseUrl += APIValues.BillingServer.CONTEXT_PATH;
//        
//        //eureka service discovery
//        //ProjectWideConstants.ServiceDiscovery.ServiceNameWithContext.BILLING; //eureka
//
//
//        //todo: //policy: ip ;ast 3 digit + thread number?
//            //note: proper policy?
//
//
//        //String tmpUniqueBatchId =
//        //        ""
//        //        + PREFIX_OF_EACH__BATCH_ID_OF_SCHEDULER
//        //        + UUID.randomUUID().toString().toUpperCase().substring(0, 5);
//        String tmpUniqueBatchId = SmartMeterProjectUtils.createBatchId(0);
//        String tmpUniqueBatchSize = "5";
//        
//        String fullGET_Url =
//                ""
//                + "http://"
//                //+ "https://"
//                + billingServerBaseUrl
//                + SCHEDULER_CONTROLLER_BASE_PATH
//                + GET_ACCOUNTS_TO_BE_UPDATED
//                + "?" + REQUEST_PARAMETER__DAY + "=" + DynamicGlobals.todayString
//                + "&" + REQUEST_PARAMETER__BATCH_ID + "=" + tmpUniqueBatchId
//                + "&" + REQUEST_PARAMETER__BATCH_SIZE + "=" + tmpUniqueBatchSize;
//
//        //String getUrl_Host = billingServerBaseUrl;
//        //String getUrl_Path =
//        //        SCHEDULER_CONTROLLER_BASE_PATH
//        //      + GET_ACCOUNTS_TO_BE_UPDATED;
//
//        //#region <DevMU>
//        webClient.get()
//                .uri(fullGET_Url)
//                //.uri(builder -> builder.scheme("http")
//                //        .host(getUrl_Host).path(getUrl_Path)
//                //        .queryParam(REQUEST_PARAMETER__DAY, DynamicGlobals.todayString)
//                //        .queryParam(REQUEST_PARAMETER__BATCH_ID, tmpUniqueBatchId)
//                //        .build())
//                .accept(APPLICATION_JSON)
//
//                //todo:
//                .retrieve()
//                .bodyToMono(GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse.class)
//
//                //.exchange()
//                //.flatMap(response -> response.bodyToMono(GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse.class))
//
//                .subscribe(this::processTheseAccounts);
//        //#endregion <DevMU>
//
//        ////#region <DevTJ>
//        ////webClient.get()
//        ////        .uri(fullGET_Url)
//        ////        .accept(APPLICATION_JSON)
//        ////        .exchange()
//        ////        .flatMap(response -> response.bodyToMono(GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse.class))
//        ////        .subscribe(this::processTheseAccounts);
//        //
//        //GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse
//        //        response = new GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse();
//        //RestTemplate restTemplate = new RestTemplate();
//        //response = restTemplate.getForObject(fullGET_Url,GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse.class);
//        //if(response!=null){
//        //    processTheseAccounts(response);
//        //}
//        ////#endregion <DevTJ>
//
//
//
//        //todo: on error, set iteration flag as false
//
//
//
//        /*List<BillingAccountBillingDataDTO> billingAccountBillingDataDTOList = billGenerationServiceProvider.getBillingAccountsToBeUpdatedToday();
//        if(billingAccountBillingDataDTOList == null) {
//            log.warn("billingAccountBillingDataDTOList == null");
//            return;
//        }
//        log.warn("");
//
//        List<String> meterIds = new ArrayList<>();
//        billingAccountBillingDataDTOList
//                .forEach(billingAccountBillingDataDTO -> {
//                    meterIds.add(billingAccountBillingDataDTO.getMeterId());
//                    log.info("");
//                });
//
//        if(meterIds.size() == 0) {
//            log.warn("meterIds.size() == 0");
//            return;
//        }*/
//    }
//
//    private void processTheseAccounts(GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse response) {
//
//        //TMP
//        //inMemoryMap_METERID_vs_BillingAccountId.clear();
//
//        //List<String> meterIds = new ArrayList<>();
//
//        List<BillingAccountIdWithMeterId> billingAccountIdWithMeterIdList = new ArrayList<>();
//
//
//        response
//                .getList()
//                .forEach(billingAccountBillingDataDTO -> {
//
//                    BillingAccountIdWithMeterId billingAccountIdWithMeterId = new BillingAccountIdWithMeterId();
//
//                    //[<#custom_todo#>]
//                    //needs heavy testing
//                    //billingAccountIdWithMeterId.setBillingAccountId(billingAccountBillingDataDTO.getAccountNumber());
//                    billingAccountIdWithMeterId.setAccountNumber(billingAccountBillingDataDTO.getAccountNumber());
//                    billingAccountIdWithMeterId.setMeterDeviceId(billingAccountBillingDataDTO.getMeterId());
//                    
//                    billingAccountIdWithMeterIdList.add(billingAccountIdWithMeterId);
//                    //log.info("");
//                });
//
//        if(billingAccountIdWithMeterIdList.size() == 0) {
//            log.warn("billingAccountIdWithMeterIdList.size() == 0");
//            proceedToNextIterationOfTheScheduler();
//            return;
//        }
//
//        //Making async call to producer server to fetch latest Billing Tier Data for these meters (batch request)
//        //WebClient webClient = WebClient.create();
//        WebClient webClient = webClientBuilder.build(); //eureka
//
//        String producerServerBaseUrl = customPropertyResolver.getProducerServerIpWithPort();
//        //producerServerBaseUrl = customPropertyResolver.getProducerServerIpWithPort() + "/producer";
//        producerServerBaseUrl += APIValues.ProducerServer.CONTEXT_PATH;
//        
//        String fullPOST_Url = "http://"
//                //+ ProjectWideConstants.ServiceDiscovery.ServiceNameWithContext.PRODUCER
//                + producerServerBaseUrl
//                + BillingDataProvider_BASE_PATH
//                + METERS_READING;
//
//        GetBatchBillingDataOnADateRequest request = new GetBatchBillingDataOnADateRequest();
//        request.setBillingAccountIdWithMeterIdList(billingAccountIdWithMeterIdList);
//        request.setDateString(response.getDateString());
//
//        //todo: INJECT AUTH INFO
//
//        //#region <DevMU>
//        webClient.post()
//                .uri(fullPOST_Url)
//                .body(Mono.just(request), GetBatchBillingDataOnADateRequest.class)
//                .accept(APPLICATION_JSON)
//                .headers(headers -> headers.setBasicAuth(producerServerApiUserName, producerServerApiUserPassword))
//                .retrieve()
//                .bodyToMono(GetBatchBillingDataOnADateResponse.class)
//                .subscribe(this::processTheseBatchBillingData);
//        //#region <DevMU>
//
//
//        ////#region <DevTJ>
//        ////webClient.post()
//        ////        .uri(fullPOST_Url)
//        ////        .body(Mono.just(request), GetBatchBillingDataOnADateRequest.class)
//        ////        .accept(APPLICATION_JSON)
//        ////        .headers(headers -> headers.setBasicAuth(producerServerApiUserName, producerServerApiUserPassword))
//        ////        .retrieve()
//        ////        .bodyToMono(GetBatchBillingDataOnADateResponse.class)
//        ////        .subscribe(this::processTheseBatchBillingData);
//        //
//        ////call producer API
//        //GetBatchBillingDataOnADateResponse producerResponse = new GetBatchBillingDataOnADateResponse();
//        //RestTemplate prodTemplate = new RestTemplate();
//        //prodTemplate.getInterceptors().add(new BasicAuthenticationInterceptor("tigerit","eyetea"));
//        //producerResponse = prodTemplate.postForObject(fullPOST_Url,request,GetBatchBillingDataOnADateResponse.class);
//        //if(producerResponse!=null){
//        //    processTheseBatchBillingData(producerResponse);
//        //}
//        ////#endregion <DevTJ>
//
//    }
//
//    private void processTheseBatchBillingData(GetBatchBillingDataOnADateResponse response2) {
//
//        if(response2 == null || response2.getDataList() == null || response2.getDataList().size() == 0) {
//            log.warn("response2 == null || response2.getDataList() == null || response2.getDataList().size() == 0"); //add logs
//            proceedToNextIterationOfTheScheduler();
//            return;
//        }
//
//        String dateString = response2.getDateString();
//
//        //Generate and insert new billing records and insert in the MySQL db by calling an api exposed in billing server
//        //With no AUTH for now, since billing server has no auth. Only producer server asks for auth
//
//
//
//        //WebClient webClient = WebClient.create();
//        WebClient webClient = webClientBuilder.build(); //eureka
//
//        String billingServerBaseUrl = customPropertyResolver.getBillingServerIpWithPort(); //eg: "localhost:8081"
//        //billingServerBaseUrl = customPropertyResolver.getBillingServerIpWithPort() + "/billing";
//        billingServerBaseUrl += APIValues.BillingServer.CONTEXT_PATH;
//        
//        String fullPOST_Url = "http://"
//                + billingServerBaseUrl
//                + SCHEDULER_CONTROLLER_BASE_PATH
//                + POST_NEW_BILLING_RECORDS_TO_BE_UPDATED;
//
//        GeneratePayableBillingRecordRequest request = new GeneratePayableBillingRecordRequest();
//        request.setDayValue(dateString);
//        request.setDataList(response2.getDataList());
//
//        //call billing server rest api
//
//        //#region <DevMU>
//        webClient.post()
//                .uri(fullPOST_Url)
//                .body(Mono.just(request), GeneratePayableBillingRecordRequest.class)
//                .accept(APPLICATION_JSON)
//                .retrieve()
//                .bodyToMono(GenerateBillingRecordResponse.class)
//                .subscribe(response3 -> {
//                    //just check the status
//                    log.info(response3.getStatus());
//
//                    //Re iterate the scheduler
//                    proceedToNextIterationOfTheScheduler();
//                });
//        //#endregion <DevMU>
//
//
////        //#region <DevTJ>
////        GenerateBillingRecordResponse generateBillingRecordResponse = new GenerateBillingRecordResponse();
////        RestTemplate billTemplate = new RestTemplate();
////        generateBillingRecordResponse = billTemplate.postForObject(fullPOST_Url,request
////                ,GenerateBillingRecordResponse.class);
////        log.info(generateBillingRecordResponse.getStatus());
////
////        //webClient.post()
////        //        .uri(fullPOST_Url)
////        //        .body(Mono.just(request), GenerateBillingRecordRequest.class)
////        //        .accept(APPLICATION_JSON)
////        //        .retrieve()
////        //        .bodyToMono(GenerateBillingRecordResponse.class)
////        //        .subscribe(response3 -> {
////        //            //just check the status
////        //            log.info(response3.getStatus());
////
////                    //Re iterate the scheduler
////                    proceedToNextIterationOfTheScheduler();
//////                });
////        //#endregion <DevTJ>
//
//
//
//    }
//
//    //private void turnOffScheduler() {
//    //    isIterationRunning = true;
//    //}
//
//    private void proceedToNextIterationOfTheScheduler() {
//        isIterationRunning = false;
//
//        //for now, deciding to use this place as simulation's next iteration allowing place
//        //if(simulationCallback != null) {
//        //    simulationCallback.updateSimulation(ONE_ITERATION_DONE_SUCCESSFULLY);
//        //}
//    }
//}
