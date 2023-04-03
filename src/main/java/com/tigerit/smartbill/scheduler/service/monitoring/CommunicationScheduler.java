package com.tigerit.smartbill.scheduler.service.monitoring;


import com.fasterxml.jackson.core.type.TypeReference;
import com.tigerit.smartbill.common.dto.CustomerResponse;
import com.tigerit.smartbill.common.dto.EmailResponse;
import com.tigerit.smartbill.common.model.api.response.common.GenericAPIResponse;
import com.tigerit.smartbill.common.model.dto.CustomersToBeNotifiedRequestDto;
import com.tigerit.smartbill.common.model.dto.EmailRequestDTO;
import com.tigerit.smartbill.common.model.dto.SMSBodyRequestDTO;
import com.tigerit.smartbill.common.util.DynamicGlobals;
import com.tigerit.smartbill.common.util.ResponseConverter;
import com.tigerit.smartbill.common.util.Utils;
import com.tigerit.smartbill.common.values.APIValues;
import com.tigerit.smartbill.common.values.ProjectWideConstants;
import com.tigerit.smartbill.scheduler.model.BaseScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tigerit.smartbill.common.values.APIValues.BillingServer.Scheduler.*;
import static com.tigerit.smartbill.common.values.APIValues.CommunicationServer.EmailAndSMSProvider.*;

@Component
@Slf4j
public class CommunicationScheduler extends BaseScheduler {

    public void InitEmailAnsSMSSendScheduler() {
        //Setting up today value
        if (DynamicGlobals.useActualTodayValue) {
            String actualTodayValue = ""; //eg: format: 3/5/2019
            Date date = new Date();
            actualTodayValue = Utils.getDayStringFromJavaDate(date);
            DynamicGlobals.todayString = actualTodayValue;
        }

        if (DynamicGlobals.todayString.isEmpty()) {
            log.error("Date information is not available ");
            log.info("exiting thread");
            return;
        }

        log.info("Email and SMS sent service invoked", Thread.currentThread().getName());

        /*
         retrieve comm-service instance from eureka
         */
        ServiceInstance commService = loadBalancerClient.choose(ProjectWideConstants.ServiceDiscovery.ServiceName.COMM_SERVICE);
        ServiceInstance billingService = loadBalancerClient.choose(ProjectWideConstants.ServiceDiscovery.ServiceName.BILLING_SERVICE);

//        String billingServerBaseUrl = customAppProperties.getBillingServerWithPort();
        String billingServerBaseUrl = billingService.getUri().toString();
        billingServerBaseUrl += APIValues.BillingServer.CONTEXT_PATH;

        String billingUsername = customAppProperties.getBillingUsername(); //"test"
        String billingPassword = customAppProperties.getBillingPassword(); //"test123"

//        String commServerBaseUrl = customAppProperties.getCommunicationServerWithPort();
        String commServerBaseUrl = commService.getUri().toString();
        commServerBaseUrl += APIValues.CommunicationServer.CONTEXT_PATH;

        String commUsername = customAppProperties.getCommunicationUsername(); //"test"
        String commPassword = customAppProperties.getCommunicationPassword(); //"test123"

        String fullGET_Url = billingServerBaseUrl
                + SCHEDULER_CONTROLLER_BASE_PATH
                + GET_ACCOUNTS_TO_BE_NOTIFIED
                + "?dateToNotify=" + DynamicGlobals.todayString; // fix todayString value

        Object customersPayload = null;

//        RestTemplate accountsRestTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(billingUsername, billingPassword));

        try {
            ResponseEntity<GenericAPIResponse> customersResponse = restTemplate.exchange(fullGET_Url, HttpMethod.GET, null, GenericAPIResponse.class);
            //customers = restTemplate.getForObject (fullGET_Url, CustomersToBeNotifiedRequestDtoList.class);
            customersPayload = ResponseConverter.convertResponseToPayload(customersResponse);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return;
        }

        if (customersPayload != null) {
            /*
            convert customersPayload to email-send DTO
             */
            List<CustomersToBeNotifiedRequestDto> list = ResponseConverter.convertPayloadToCustomObjectList(
                    customersPayload, new TypeReference<List<CustomersToBeNotifiedRequestDto>>() {
                    });

            List<EmailRequestDTO> emailRequestDTOList = new ArrayList<EmailRequestDTO>();
            List<SMSBodyRequestDTO> smsBodyRequestDTOList = new ArrayList<SMSBodyRequestDTO>();

            list.forEach(
                    item -> {
                        if (item.getMailBody() != null) {
                            EmailRequestDTO emailRequestDTO = new EmailRequestDTO();

//                            emailRequestDTO.setPayableBillingRecord(item.getPayableBillingRecord());
//                            emailRequestDTO.setFrom("Test");
                            emailRequestDTO.setToAddresses(item.getCustomerEmailAddress());
                            emailRequestDTO.setSubject("Monthly Bill");
                            emailRequestDTO.setBody(item.getMailBody());
                            emailRequestDTO.setFileContent(item.getContent());
                            emailRequestDTO.setFileName(item.getFileName());

                            emailRequestDTOList.add(emailRequestDTO);
                        }

                        if (item.getSmsBody() != null) {
                            SMSBodyRequestDTO smsBodyRequestDTO = new SMSBodyRequestDTO();

//                            smsBodyRequestDTO.setPayableBillingRecord(item.getPayableBillingRecord());
                            smsBodyRequestDTO.setBody(item.getSmsBody());
                            smsBodyRequestDTO.setToMobile(item.getCustomerMobileNo());
//                            smsBodyRequestDTO.setFrom("");

                            smsBodyRequestDTOList.add(smsBodyRequestDTO);
                        }
                    }
            );

            /**
             * Comm server API endpoint
             */
            String fullEmailPOST_Url = commServerBaseUrl + EmailAndSMSProvider_BASE_PATH + SEND_BATCH_MAIL;


            if (emailRequestDTOList.size() > 0) {
//                RestTemplate emailSendTemplate = new RestTemplate();
                restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(commUsername, commPassword));

                Object emailRequestPayload = null;

                try {
                    ResponseEntity<GenericAPIResponse> emailRequestResponse = restTemplate.exchange(
                            fullEmailPOST_Url, HttpMethod.POST, new HttpEntity<>(emailRequestDTOList), GenericAPIResponse.class);
                    emailRequestPayload = ResponseConverter.convertResponseToPayload(emailRequestResponse);
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }

                if (emailRequestPayload != null) {
                    CustomerResponse response = ResponseConverter.convertPayloadToCustomObject(emailRequestPayload, CustomerResponse.class);

                    String fullNotifiedPOST_Url = billingServerBaseUrl + SCHEDULER_CONTROLLER_BASE_PATH + UPDATE_NOTIFIED_ACCOUNTS_MAIL;

//                    RestTemplate notifiedRestTemplate = new RestTemplate();
                    restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(billingUsername, billingPassword));

                    Object mailNotifiedPayload = null;

                    List<EmailResponse> notifiedRequest = response.getEmailResponseList();

                    try {
                        ResponseEntity<GenericAPIResponse> mailNotifiedResponse = restTemplate.exchange(fullNotifiedPOST_Url, HttpMethod.POST, new HttpEntity<>(notifiedRequest), GenericAPIResponse.class);
                        //notifiedResponse = notifiedRestTemplate.postForObject (fullNotifiedPOST_Url, notifiedRequest, GenericAPIResponse.class);
                        mailNotifiedPayload = ResponseConverter.convertResponseToPayload(mailNotifiedResponse);
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }

                    if (mailNotifiedPayload != null) {
                    }
                }
            }

            //call sms api
            final String fullSMSPOST_Url = commServerBaseUrl + EmailAndSMSProvider_BASE_PATH + SEND_BATCH_SMS;

            if (smsBodyRequestDTOList.size() > 0) {
//                RestTemplate smsSendTemplate = new RestTemplate();
                restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(commUsername, commPassword));

                Object smsPayload = null;

                try {
                    ResponseEntity<GenericAPIResponse> smsResponse = restTemplate.exchange(fullSMSPOST_Url, HttpMethod.POST, new HttpEntity<>(smsBodyRequestDTOList), GenericAPIResponse.class);
                    smsPayload = ResponseConverter.convertResponseToPayload(smsResponse);
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }

                if (smsPayload != null) {
                    CustomerResponse response = ResponseConverter.convertPayloadToCustomObject(smsPayload, CustomerResponse.class);

                    String fullNotifiedPOST_Url = billingServerBaseUrl + SCHEDULER_CONTROLLER_BASE_PATH + UPDATE_NOTIFIED_ACCOUNTS_SMS;

//                    RestTemplate notifiedRestTemplate = new RestTemplate();
                    restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(billingUsername, billingPassword));

                    Object notifiedPayload = null;

                    List<EmailResponse> notifiedRequest = response.getEmailResponseList();

                    try {
                        ResponseEntity<GenericAPIResponse> smsNotifiedResponse = restTemplate.exchange(fullNotifiedPOST_Url, HttpMethod.POST, new HttpEntity<>(notifiedRequest), GenericAPIResponse.class);
                        notifiedPayload = ResponseConverter.convertResponseToPayload(smsNotifiedResponse);
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }

                    if (notifiedPayload != null) {
                    }
                }
            }
        }
    }

    public void generatePDFScheduler() {
        //Setting up today value
        if (DynamicGlobals.useActualTodayValue) {
            String actualTodayValue = ""; //eg: format: 3/5/2019
            Date date = new Date();
            actualTodayValue = Utils.getDayStringFromJavaDate(date);
            DynamicGlobals.todayString = actualTodayValue;
        }

        if (DynamicGlobals.todayString.isEmpty()) {
            log.error("Date value is empty!!!");
            log.info("exiting thread");
            return;
        }

        ServiceInstance serviceInstance = loadBalancerClient.choose(ProjectWideConstants.ServiceDiscovery.ServiceName.BILLING_SERVICE);
        String billingServerBaseUrl = serviceInstance.getUri().toString();
        billingServerBaseUrl += APIValues.BillingServer.CONTEXT_PATH;

        String billingUsername = customAppProperties.getBillingUsername(); //"test"
        String billingPassword = customAppProperties.getBillingPassword(); //"test123"

        String fullPOST_Url = billingServerBaseUrl + SCHEDULER_CONTROLLER_BASE_PATH + GET_PDF_BILLS_TO_BE_CREATED
                + "?today=" + DynamicGlobals.todayString; // fix todayString value

        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(billingUsername, billingPassword));

        Object payload = null;

        try {
            ResponseEntity<GenericAPIResponse> response = restTemplate.exchange(
                    fullPOST_Url, HttpMethod.POST, null, GenericAPIResponse.class);
            payload = ResponseConverter.convertResponseToPayload(response);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        if (payload != null) {
        }
    }

}
