//package com.tigerit.smartbill.scheduler.service.monitoring;
//
//import com.tigerit.smartbill.common.model.api.request.scheduler.GetMetadataOfBillingAccountsTobeUpdatedOnThisDayRequest;
//import com.tigerit.smartbill.common.model.dto.BillingAccountBillingDataDTO;
//import com.tigerit.smartbill.scheduler.service.NetworkServiceProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class BillGenerationServiceProvider {
//
//    //tmp
//    @Value(value = "${toDayString}")
//    String toDayString;
//
//    NetworkServiceProvider networkServiceProvider;
//
//    @Autowired
//    public void setNetworkServiceProvider(NetworkServiceProvider networkServiceProvider) {
//        this.networkServiceProvider = networkServiceProvider;
//    }
//
//    List<BillingAccountBillingDataDTO> getBillingAccountsToBeUpdatedToday() {
//        List<BillingAccountBillingDataDTO> list = new ArrayList<>();
//
//        //url
//        //String getUrl = "";
//
//        //request
//        GetMetadataOfBillingAccountsTobeUpdatedOnThisDayRequest request = new GetMetadataOfBillingAccountsTobeUpdatedOnThisDayRequest();
//        String todayString_ddMMyyyy = toDayString;
//        request.setDdMMYYYY_dateString(todayString_ddMMyyyy);
//
//        //GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse response;
//        //response = networkServiceProvider.
//        //response
//
//        return list;
//    }
//
//}
