package com.tigerit.smartbill.scheduler.service;

import com.tigerit.smartbill.scheduler.service.network.async.AsyncNetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NetworkServiceProvider {

    AsyncNetworkService asyncNetworkService;

    @Autowired
    public void setAsyncNetworkService(AsyncNetworkService asyncNetworkService) {
        this.asyncNetworkService = asyncNetworkService;
    }

    /*RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }*/

    /*GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse getAccountsToBeUpdatedOnThisDay(GetMetadataOfBillingAccountsTobeUpdatedOnThisDayRequest request) {
        List<String> meterIds = new ArrayList<>();
        GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse response = new GetMetadataOfBillingAccountsTobeUpdatedOnThisDayResponse();
        response
                .getList()
                .forEach(billingAccountBillingDataDTO -> {
                    meterIds.add(billingAccountBillingDataDTO.getMeterId());
                });
        return meterIds;
    }*/

    //Generic Post method
    Object getAsync(String getUrl, Object requestObject, Class<?> responseObjectType) {
        Object responseObject;
        responseObject = asyncNetworkService.getAsync(getUrl, requestObject, responseObjectType);
        return responseObject;
    }


    //Generic Post method
    Object postAsync(String postUrl, Object requestObject, Class<?> responseObjectType) {
        //return restTemplate.postForObject(postUrl, requestObject, responseObjectType);
        Object responseObject = new Object();

        return responseObject;
    }
}
