package com.tigerit.smartbill.scheduler.controller;

import com.tigerit.smartbill.common.util.Utils;
import com.tigerit.smartbill.common.values.APIValues;
import com.tigerit.smartbill.common.values.ProjectWideConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static com.tigerit.smartbill.common.util.Constants.LOG_MSG;
import static com.tigerit.smartbill.common.values.APIValues.EpiCoreServer.EurekaTestController.A_CALL_FROM_SCHEDULER;
import static com.tigerit.smartbill.common.values.APIValues.SchedulerServer.EurekaTestController.GET_DUMMY_MSG;
import static com.tigerit.smartbill.common.values.ProjectWideConstants.ServiceDiscovery.LOAD_BALANCER_BEAN;


@Slf4j
@RequestMapping(
        //"/test/eureka"
        APIValues.SchedulerServer.EurekaTestController.RELEVANT_NAME
)
@RestController
public class EurekaCommunicationTestingController {

    private LoadBalancerClient loadBalancerClient;
    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(@Qualifier(LOAD_BALANCER_BEAN) RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setLoadBalancerClient(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    // "/dummy-msg"
    @GetMapping(GET_DUMMY_MSG)
    public String getDummyString() {
        String  str = "<<<a string from scheduler>>>";
        return str;
    }

    // "/call-epicore-from-scheduler")
    @GetMapping(A_CALL_FROM_SCHEDULER)
    public String callEpiCore() {
        String  str = "nothing!";// = "<<<a msg from epicore to scheduler>>>";
        String fullUrl = "";
        try {
             fullUrl = getEpiCoreBaseUrl()
                        + "/"
                             + ProjectWideConstants.ProjectModule.EPICORE
                             + APIValues.EpiCoreServer.EurekaTestController.RELEVANT_NAME
                             + APIValues.EpiCoreServer.EurekaTestController.A_CALL_FROM_SCHEDULER;
            str = restTemplate.getForObject(
                    fullUrl, String.class
            );
        } catch (Exception e) {
            LOG_MSG = "";
            LOG_MSG = "error when calling epicore from scheduler";
            log.error(Utils.prepareLogMessage(LOG_MSG),e);
        }
        return str;
    }

    public String getEpiCoreBaseUrl() {
        ServiceInstance serviceInstance = loadBalancerClient.choose(
                ProjectWideConstants.ServiceDiscovery.ServiceName.EPICORE_SERVICE
        );
        String uri = serviceInstance.getUri().toString();
        //String host = serviceInstance.getHost();
        //String port = String.valueOf(serviceInstance.getPort());
        return uri;
    }
}
