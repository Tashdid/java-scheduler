package com.tigerit.smartbill.scheduler.service.monitoring;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tigerit.smartbill.common.dto.CustomerResponse;
import com.tigerit.smartbill.common.dto.EmailResponse;
import com.tigerit.smartbill.common.model.dto.CustomersToBeNotifiedRequestDto;
import com.tigerit.smartbill.common.model.dto.EmailPayableBillingRecordRequestDTO;
import com.tigerit.smartbill.common.model.dto.SMSBodyRequestDTO;
import com.tigerit.smartbill.common.util.DynamicGlobals;
import com.tigerit.smartbill.common.util.ResponseConverter;
import com.tigerit.smartbill.common.util.Utils;
import com.tigerit.smartbill.scheduler.config.props.CustomAppProperties;
import com.tigerit.smartbill.scheduler.service.network.TigerITBillingServer;
import com.tigerit.smartbill.scheduler.service.network.TigerITCommunicationServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tigerit.smartbill.common.values.APIValues.BillingServer.Scheduler.*;
import static com.tigerit.smartbill.common.values.APIValues.CommunicationServer.EmailAndSMSProvider.*;

@Slf4j
@Component
public class NotificationScheduler {

    @Autowired
    CustomAppProperties customAppProperties;

    public void InitEmailAnsSMSSendScheduler() {
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

        log.info("Email and SMS sent service invoked", Thread.currentThread().getName());

        String accountsNotifiedEndpoint = SCHEDULER_CONTROLLER_BASE_PATH
                + GET_ACCOUNTS_TO_BE_NOTIFIED
                + "?dateToNotify=" + DynamicGlobals.todayString; // fix todayString value

        TigerITBillingServer tigerITBillingServer = new TigerITBillingServer();
        Object customersPayload = tigerITBillingServer.getData(accountsNotifiedEndpoint);
/*        
        String billingServerBaseUrl = customAppProperties.getBillingServerWithPort(); //"192.168.106.213:10081/billing"; //eg: "localhost:8081"
        billingServerBaseUrl += APIValues.BillingServer.CONTEXT_PATH;
        
        String billingUsername = customAppProperties.getBillingUsername(); //"test"
        String billingPassword = customAppProperties.getBillingPassword(); //"test123"

        String fullGET_Url = "http://"
                + billingServerBaseUrl
                + SCHEDULER_CONTROLLER_BASE_PATH
                + GET_ACCOUNTS_TO_BE_NOTIFIED
                + "?dateToNotify=" + DynamicGlobals.todayString; // fix todayString value

        Object customersPayload = null;
        
        RestTemplate accountsRestTemplate = new RestTemplate ();
        accountsRestTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(billingUsername, billingPassword));
        
        try {
        	ResponseEntity<GenericAPIResponse> customersResponse = accountsRestTemplate.exchange(fullGET_Url, HttpMethod.GET, null, GenericAPIResponse.class);
            //customers = restTemplate.getForObject (fullGET_Url, CustomersToBeNotifiedRequestDtoList.class);
        	customersPayload = ResponseConverter.convertResponseToPayload(customersResponse);
        }
        catch (Exception ex) {
        	log.error (ex.getMessage());
        	return;
        }
*/

/*        
        String commServerBaseUrl = customAppProperties.getCommunicationServerWithPort(); //"localhost:8086/commserver";
        commServerBaseUrl += APIValues.CommunicationServer.CONTEXT_PATH;
        
        String commUsername = customAppProperties.getCommunicationUsername(); //"test"
        String commPassword = customAppProperties.getCommunicationPassword(); //"test123"
*/
        if (customersPayload != null) {
            List<CustomersToBeNotifiedRequestDto> list = ResponseConverter.convertPayloadToCustomObjectList(customersPayload, new TypeReference<List<CustomersToBeNotifiedRequestDto>>() {
            });

            List<EmailPayableBillingRecordRequestDTO> emailRequestDTOList = new ArrayList<EmailPayableBillingRecordRequestDTO>();
            List<SMSBodyRequestDTO> smsBodyRequestDTOList = new ArrayList<SMSBodyRequestDTO>();

            list.forEach(
                    item -> {
                        if (item.getMailBody() != null) {
                            EmailPayableBillingRecordRequestDTO emailRequestDTO = new EmailPayableBillingRecordRequestDTO();

                            //emailRequestDTO.setPayableBillingRecord(item.getPayableBillingRecord());
                            //emailRequestDTO.setFrom ("Test");
                            emailRequestDTO.setToAddresses(item.getCustomerEmailAddress());
                            emailRequestDTO.setSubject("Monthly Bill");
                            emailRequestDTO.setBody(item.getMailBody());
                            emailRequestDTO.setFileContent(item.getContent());
                            emailRequestDTO.setFileName(item.getFileName());

                            emailRequestDTOList.add(emailRequestDTO);
                        }

                        if (item.getSmsBody() != null) {
                            SMSBodyRequestDTO smsBodyRequestDTO = new SMSBodyRequestDTO();

                            //smsBodyRequestDTO.setPayableBillingRecord(item.getPayableBillingRecord());
                            smsBodyRequestDTO.setBody(item.getSmsBody());
                            smsBodyRequestDTO.setToMobile(item.getCustomerMobileNo());
                            //smsBodyRequestDTO.setFrom ("");

                            smsBodyRequestDTOList.add(smsBodyRequestDTO);
                        }
                    }
            );

            //call email api
            if (emailRequestDTOList.size() > 0) {
                String batchMailEndpoint = EmailAndSMSProvider_BASE_PATH
                        + SEND_BATCH_MAIL;

                TigerITCommunicationServer tigerITCommunicationServer = new TigerITCommunicationServer();
                Object emailRequestPayload = tigerITCommunicationServer.postData(batchMailEndpoint, new HttpEntity<>(emailRequestDTOList));
/*		        
		        String fullEmailPOST_Url = "http://"
		                + commServerBaseUrl
		                + EmailAndSMSProvider_BASE_PATH
		                + SEND_BATCH_MAIL;

	            RestTemplate emailSendTemplate = new RestTemplate ();
	            emailSendTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(commUsername, commPassword));
	            
	            Object emailRequestPayload = null;
	
	            try {	                	
	            	ResponseEntity<GenericAPIResponse> emailRequestResponse = emailSendTemplate.exchange(fullEmailPOST_Url, HttpMethod.POST, new HttpEntity<>(emailRequestDTOList), GenericAPIResponse.class);	                	
	                //mailResponse = emailSendTemplate.postForObject (fullEmailPOST_Url, emailRequestDTOList, GenericAPIResponse.class);
	            	emailRequestPayload = ResponseConverter.convertResponseToPayload(emailRequestResponse);
	            } catch (Exception ex) {
	            	log.error (ex.getMessage());
	            }
*/
                if (emailRequestPayload != null) {
                    CustomerResponse response = ResponseConverter.convertPayloadToCustomObject(emailRequestPayload, CustomerResponse.class);
                    List<EmailResponse> notifiedRequest = response.getEmailResponseList();

                    String updateAccountsEndpoint = SCHEDULER_CONTROLLER_BASE_PATH
                            + UPDATE_NOTIFIED_ACCOUNTS_MAIL;

                    Object mailNotifiedPayload = tigerITBillingServer.postData(updateAccountsEndpoint, new HttpEntity<>(notifiedRequest));
/*	            	
	            	String fullNotifiedPOST_Url = "http://"
	            			+ billingServerBaseUrl
	                        + SCHEDULER_CONTROLLER_BASE_PATH
	                        + UPDATE_NOTIFIED_ACCOUNTS_MAIL;
	                
	            	RestTemplate notifiedRestTemplate = new RestTemplate ();
	            	notifiedRestTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(billingUsername, billingPassword));
	            	
	            	Object mailNotifiedPayload = null;
		            
	                try {
	                	ResponseEntity<GenericAPIResponse> mailNotifiedResponse = notifiedRestTemplate.exchange(fullNotifiedPOST_Url, HttpMethod.POST, new HttpEntity<>(notifiedRequest), GenericAPIResponse.class);
	                	//notifiedResponse = notifiedRestTemplate.postForObject (fullNotifiedPOST_Url, notifiedRequest, GenericAPIResponse.class);
	                	mailNotifiedPayload = ResponseConverter.convertResponseToPayload(mailNotifiedResponse);
	                } catch (Exception ex) {
	                	log.error (ex.getMessage());
	                }
*/
                    if (mailNotifiedPayload != null) {
                    }
                }
            }

            //call sms api
            if (smsBodyRequestDTOList.size() > 0) {
                String batchSmsEndpoint = EmailAndSMSProvider_BASE_PATH
                        + SEND_BATCH_SMS;

                TigerITCommunicationServer tigerITCommunicationServer = new TigerITCommunicationServer();
                Object smsPayload = tigerITCommunicationServer.postData(batchSmsEndpoint, new HttpEntity<>(smsBodyRequestDTOList));
/*
		        String fullSMSPOST_Url = "http://"
		                + commServerBaseUrl
		                + EmailAndSMSProvider_BASE_PATH
		                + SEND_BATCH_SMS;

	            RestTemplate smsSendTemplate = new RestTemplate ();
	            smsSendTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(commUsername, commPassword));
	            
	            Object smsPayload = null;
	            
	            try {
	            	ResponseEntity<GenericAPIResponse> smsResponse = smsSendTemplate.exchange(fullSMSPOST_Url, HttpMethod.POST, new HttpEntity<>(smsBodyRequestDTOList), GenericAPIResponse.class);
	            	//smsResponse = smsSendTemplate.postForObject (fullSMSPOST_Url, smsBodyRequestDTOList, GenericAPIResponse.class);
	            	smsPayload = ResponseConverter.convertResponseToPayload(smsResponse);
	            } catch (Exception ex) {
	                log.error (ex.getMessage());
	            }
*/
                if (smsPayload != null) {
                    CustomerResponse response = ResponseConverter.convertPayloadToCustomObject(smsPayload, CustomerResponse.class);
                    List<EmailResponse> notifiedRequest = response.getEmailResponseList();

                    String updateAccountsEndpoint = SCHEDULER_CONTROLLER_BASE_PATH
                            + UPDATE_NOTIFIED_ACCOUNTS_SMS;

                    Object smsNotifiedPayload = tigerITBillingServer.postData(updateAccountsEndpoint, new HttpEntity<>(notifiedRequest));
/*
	            	String fullNotifiedPOST_Url = "http://"
	            			+ billingServerBaseUrl
	                        + SCHEDULER_CONTROLLER_BASE_PATH
	                        + UPDATE_NOTIFIED_ACCOUNTS_SMS;
	                
	            	RestTemplate notifiedRestTemplate = new RestTemplate ();
	            	notifiedRestTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(billingUsername, billingPassword));
	            	
	            	Object smsNotifiedPayload = null;
	                
	                try {
	                	ResponseEntity<GenericAPIResponse> smsNotifiedResponse = notifiedRestTemplate.exchange(fullNotifiedPOST_Url, HttpMethod.POST, new HttpEntity<>(notifiedRequest), GenericAPIResponse.class);
	                	//notifiedResponse = notifiedRestTemplate.postForObject (fullNotifiedPOST_Url, notifiedRequest, GenericAPIResponse.class);
	                	smsNotifiedPayload = ResponseConverter.convertResponseToPayload(smsNotifiedResponse);
	                } catch (Exception ex) {
	                	log.error (ex.getMessage());
	                }
*/
                    if (smsNotifiedPayload != null) {
                    }
                }
            }
        }
    }
}