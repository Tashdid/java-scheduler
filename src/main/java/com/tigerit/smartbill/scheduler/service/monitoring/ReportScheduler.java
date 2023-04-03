//package com.tigerit.smartbill.scheduler.service.monitoring;
//
//import com.tigerit.smartbill.common.model.dto.ReportResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//@Component
//@Slf4j
//public class ReportScheduler {
//
//    private boolean isRecordInserted = false;
//
//    public void DailyReportGenerate() {
//
//        if (!isRecordInserted) {
//            isRecordInserted = true;
//            log.info ("Entering Report generation task", Thread.currentThread ().getName ());
//            String fullGET_Url =
//                    "http://localhost:8081/billing/api/v1/scheduler/reports/daily";
//
//            //ReportResponse reportResponse = new ReportResponse ();
//            //new GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse();
//            RestTemplate restTemplate = new RestTemplate ();
//            try {
//                restTemplate.getForObject (fullGET_Url,
//                        ReportResponse.class);
//            } catch (Exception ex) {
//                log.info ("[Report Generation Error] " + ex.getMessage ());
//            }
//        } else {
//            //isRecordInserted = false;
//            log.info ("-----Skipping iteration-------");
//        }
//    }
//
//    public void WeeklyReportGenerate() {
//
//        //if (true) {
////            isRecordInserted = true;
//            log.info ("Entering Report generation task", Thread.currentThread ().getName ());
//            String fullGET_Url =
//                    "http://localhost:8081/billing/api/v1/scheduler/reports/weekly";
//
//            //ReportResponse reportResponse = new ReportResponse ();
//            RestTemplate restTemplate = new RestTemplate ();
//            try {
//                restTemplate.getForObject (fullGET_Url,
//                        ReportResponse.class);
//            } catch (Exception ex) {
//                log.info ("[Report Generation Error] " + ex.getMessage ());
//            }
//        //} else {
//        //    //isRecordInserted = false;
//        //    log.info ("-----Skipping iteration-------");
//        //}
//    }
//
//    public void MonthlyReportGenerate() {
//
//        //if (true) {
////            isRecordInserted = true;
//            log.info ("Entering Report generation task", Thread.currentThread ().getName ());
//            String fullGET_Url =
//                    "http://localhost:8081/billing/api/v1/scheduler/reports/monthly";
//
//            //ReportResponse reportResponse = new ReportResponse ();
//            //new GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse();
//            RestTemplate restTemplate = new RestTemplate ();
//            try {
//                restTemplate.getForObject (fullGET_Url,
//                        ReportResponse.class);
//            } catch (Exception ex) {
//                log.info ("[Report Generation Error] " + ex.getMessage ());
//            }
//        //} else {
//        //    //isRecordInserted = false;
//        //    log.info ("-----Skipping iteration-------");
//        //}
//    }
//}
