package com.tigerit.smartbill.scheduler.model;

import com.tigerit.smartbill.scheduler.config.props.CustomAppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.client.RestTemplate;

import static com.tigerit.smartbill.common.values.ProjectWideConstants.ServiceDiscovery.LOAD_BALANCER_BEAN;

public class BaseScheduler {
    protected CustomAppProperties customAppProperties;
    protected LoadBalancerClient loadBalancerClient;
    protected RestTemplate restTemplate;

    @Autowired
    public void setCustomAppProperties(CustomAppProperties customAppProperties) {
        this.customAppProperties = customAppProperties;
    }

    @Autowired
    public void setRestTemplate(@Qualifier(LOAD_BALANCER_BEAN) RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setLoadBalancerClient(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

//    @Bean
//    ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
//                                                            LoadBalancerClientFactory loadBalancerClientFactory) {
//        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
//        return new RandomLoadBalancer(loadBalancerClientFactory
//                .getLazyProvider(name, ServiceInstanceListSupplier.class),
//                name);
//    }
}

