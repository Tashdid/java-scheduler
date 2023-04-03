package com.tigerit.smartbill.scheduler.service.network.sync;

import com.tigerit.smartbill.common.model.api.response.scheduler.GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse;
import com.tigerit.smartbill.scheduler.service.NetworkServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//todo: code in interface?
@Service
public class RemoteApiConsumer {

    NetworkServiceProvider networkServiceProvider;

    @Autowired
    public void setNetworkServiceProvider(NetworkServiceProvider networkServiceProvider) {
        this.networkServiceProvider = networkServiceProvider;
    }

    //returns meter id list
    List<String> getAccountsToBeUpdatedOnThisDay(String dayString) {
        List<String> meterIds = new ArrayList<>();

        //GetMetadataOfBillingAccountsTobeUpdatedOnThisDayRequest request;
        GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse response = new GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse();
        response
                .getList()
                .forEach(billingAccountBillingDataDTO -> {
                    meterIds.add(billingAccountBillingDataDTO.getMeterId());
                });
        return meterIds;
    }
}
