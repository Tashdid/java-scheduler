package com.tigerit.smartbill.scheduler.service.network.async;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AsyncNetworkService {
    //WebClient webClient = WebClient.create("http://localhost:8081");
    WebClient webClient = WebClient.create();

    Object postAsync(String postUrl, Object requestObject, Class<?> responseObjectType) {
        Object responseObject = new Object();
        return responseObject;
    }


    public Object getAsync(String getUrl, Object requestObject, Class<?> responseObjectType) {
        Object responseObject = new Object();
        webClient.get()
                .uri(getUrl)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> response.bodyToMono(responseObjectType));

        return responseObject;
    }
}
