package com.tigerit.smartbill.scheduler.controller;

import com.tigerit.smartbill.common.model.api.response.common.GenericAPIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.tigerit.smartbill.common.model.util.APIExposedPojoHelper.makeSuccessfulResponseWithThisData;

@RestController
public class FetchRegistryClients {

    // Case insensitive: could also use: http://accounts-service
    public static final String ACCOUNTS_SERVICE_URL
            = "http://ACCOUNTS-SERVICE";

    private DiscoveryClient discoveryClient;

//    @LoadBalanced
//    @Bean
//    RestTemplate restTemplate() {
//        return new RestTemplate();
//    }


    @Autowired
    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/get-cloud-services")
    public ResponseEntity<GenericAPIResponse> GetRegisteredClients(){
        List<String> clientServices = discoveryClient.getServices();

        return ResponseEntity.ok(makeSuccessfulResponseWithThisData(clientServices));
    }

}
