package com.tigerit.smartbill.scheduler.service.monitoring;

import com.tigerit.smartbill.common.dto.RegisterServerInfo;
import com.tigerit.smartbill.common.util.DateConvertUtils;
import com.tigerit.smartbill.scheduler.config.props.CustomAppProperties;
import com.tigerit.smartbill.scheduler.service.network.TigerITBillingServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.tigerit.smartbill.common.values.APIValues.BillingServer.Scheduler.REGISTER_SERVICE;
import static com.tigerit.smartbill.common.values.APIValues.BillingServer.Scheduler.SCHEDULER_CONTROLLER_BASE_PATH;
import static com.tigerit.smartbill.common.values.ProjectWideConstants.CustomExternalConfiguration.SchedulerConfigs.*;
import static com.tigerit.smartbill.scheduler.service.monitoring.SchedulerConfiguration.*;

@Slf4j
@EnableScheduling
@Component
public class ServerRegistrationScheduler {

    static String hostAddress = null;
    static String schedulerPort = null;
    boolean registrationSuccess = false;
    RegisterServerInfo registerServerInfo = null;

    CustomAppProperties customAppProperties;
    SchedulerConfiguration schedulerConfiguration;

    @Autowired
    @Qualifier("reservationPool")
    ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    public void setCustomAppProperties(CustomAppProperties customAppProperties) {
        this.customAppProperties = customAppProperties;
    }

    @Autowired
    public void setSchedulerConfiguration(SchedulerConfiguration schedulerConfiguration) {
        this.schedulerConfiguration = schedulerConfiguration;
    }

    /**
     * This method reads values parsed from external property file and passed to next block
     * @return
     */
    private Map<String, Integer> getEnabledSchedulerServices() {
        Map<String, Integer> schedulerServices = new HashMap<>();

        if (customAppProperties.getBillGeneration().compareTo(SCHEDULER_DISABLE) != 0) {
            if (customAppProperties.getBillGeneration().compareTo(SCHEDULER_MANUAL) == 0)

                schedulerServices.put(SCHEDULER_BILL_GENERATION, SCHEDULER_STATUS_MANUAL);
            if (customAppProperties.getBillGeneration().compareTo(SCHEDULER_AUTOMATIC) == 0)

                schedulerServices.put(SCHEDULER_BILL_GENERATION, SCHEDULER_STATUS_AUTO);
        } else {
            schedulerServices.put(SCHEDULER_BILL_GENERATION, SCHEDULER_STATUS_DISABLED);
        }
        if (customAppProperties.getPrepayStatusCheck().compareTo(SCHEDULER_DISABLE) != 0) {
            if (customAppProperties.getPrepayStatusCheck().compareTo(SCHEDULER_MANUAL) == 0)
                schedulerServices.put(SCHEDULER_PREPAY_STATUS_CHECK, SCHEDULER_STATUS_MANUAL);
            if (customAppProperties.getPrepayStatusCheck().compareTo(SCHEDULER_AUTOMATIC) == 0)
                schedulerServices.put(SCHEDULER_PREPAY_STATUS_CHECK, SCHEDULER_STATUS_AUTO);
        } else {
            schedulerServices.put(SCHEDULER_PREPAY_STATUS_CHECK, SCHEDULER_STATUS_DISABLED);
        }
        if (customAppProperties.getPdfGeneration().compareTo(SCHEDULER_DISABLE) != 0) {
            if (customAppProperties.getPdfGeneration().compareTo(SCHEDULER_MANUAL) == 0)
                schedulerServices.put(SCHEDULER_PDF_GENERATION, SCHEDULER_STATUS_MANUAL);
            if (customAppProperties.getPdfGeneration().compareTo(SCHEDULER_AUTOMATIC) == 0)
                schedulerServices.put(SCHEDULER_PDF_GENERATION, SCHEDULER_STATUS_AUTO);
        } else {
            schedulerServices.put(SCHEDULER_PDF_GENERATION, SCHEDULER_STATUS_DISABLED);
        }
        if (customAppProperties.getEmailSmsNotification().compareTo(SCHEDULER_DISABLE) != 0) {
            if (customAppProperties.getEmailSmsNotification().compareTo(SCHEDULER_MANUAL) == 0)
                schedulerServices.put(SCHEDULER_EMAIL_SMS_NOTIFICATION, SCHEDULER_STATUS_MANUAL);
            if (customAppProperties.getEmailSmsNotification().compareTo(SCHEDULER_AUTOMATIC) == 0)
                schedulerServices.put(SCHEDULER_EMAIL_SMS_NOTIFICATION, SCHEDULER_STATUS_AUTO);
        } else {
            schedulerServices.put(SCHEDULER_EMAIL_SMS_NOTIFICATION, SCHEDULER_STATUS_DISABLED);
        }
        if (customAppProperties.getDailyTransaction().compareTo(SCHEDULER_DISABLE) != 0) {
            if (customAppProperties.getDailyTransaction().compareTo(SCHEDULER_MANUAL) == 0)
                schedulerServices.put(SCHEDULER_DAILY_TRANSACTION, SCHEDULER_STATUS_MANUAL);
            if (customAppProperties.getDailyTransaction().compareTo(SCHEDULER_AUTOMATIC) == 0)
                schedulerServices.put(SCHEDULER_DAILY_TRANSACTION, SCHEDULER_STATUS_AUTO);
        } else {
            schedulerServices.put(SCHEDULER_DAILY_TRANSACTION, SCHEDULER_STATUS_DISABLED);
        }
        if (customAppProperties.getWeeklyTransaction().compareTo(SCHEDULER_DISABLE) != 0) {
            if (customAppProperties.getWeeklyTransaction().compareTo(SCHEDULER_MANUAL) == 0)
                schedulerServices.put(SCHEDULER_WEEKLY_TRANSACTION, SCHEDULER_STATUS_MANUAL);
            if (customAppProperties.getWeeklyTransaction().compareTo(SCHEDULER_AUTOMATIC) == 0)
                schedulerServices.put(SCHEDULER_WEEKLY_TRANSACTION, SCHEDULER_STATUS_AUTO);
        } else {
            schedulerServices.put(SCHEDULER_WEEKLY_TRANSACTION, SCHEDULER_STATUS_DISABLED);
        }
        if (customAppProperties.getMonthlyTransaction().compareTo(SCHEDULER_DISABLE) != 0) {
            if (customAppProperties.getMonthlyTransaction().compareTo(SCHEDULER_MANUAL) == 0)
                schedulerServices.put(SCHEDULER_MONTHLY_TRANSACTION, SCHEDULER_STATUS_MANUAL);
            if (customAppProperties.getMonthlyTransaction().compareTo(SCHEDULER_AUTOMATIC) == 0)
                schedulerServices.put(SCHEDULER_MONTHLY_TRANSACTION, SCHEDULER_STATUS_AUTO);
        } else {
            schedulerServices.put(SCHEDULER_MONTHLY_TRANSACTION, SCHEDULER_STATUS_DISABLED);
        }
        if (customAppProperties.getDataArchive().compareTo(SCHEDULER_DISABLE) != 0) {
            if (customAppProperties.getDataArchive().compareTo(SCHEDULER_MANUAL) == 0)
                schedulerServices.put(SCHEDULER_DATA_ARCHIVE, SCHEDULER_STATUS_MANUAL);
            if (customAppProperties.getDataArchive().compareTo(SCHEDULER_AUTOMATIC) == 0)
                schedulerServices.put(SCHEDULER_DATA_ARCHIVE, SCHEDULER_STATUS_AUTO);
        } else {
            schedulerServices.put(SCHEDULER_DATA_ARCHIVE, SCHEDULER_STATUS_DISABLED);
        }

        if (customAppProperties.getDummyTask().compareTo(SCHEDULER_DISABLE) != 0) {
            if (customAppProperties.getDummyTask().compareTo(SCHEDULER_MANUAL) == 0)
                schedulerServices.put(SCHEDULER_DUMMY_TASK, SCHEDULER_STATUS_MANUAL);
            if (customAppProperties.getDummyTask().compareTo(SCHEDULER_AUTOMATIC) == 0)
                schedulerServices.put(SCHEDULER_DUMMY_TASK, SCHEDULER_STATUS_AUTO);
        } else {
            schedulerServices.put(SCHEDULER_DUMMY_TASK, SCHEDULER_STATUS_DISABLED);
        }

        return schedulerServices;
    }

    @SuppressWarnings("unchecked")
    public void executeScheduler() {
        if (registerServerInfo == null) registerServerInfo = ResolveServerInfo();
        if (registerServerInfo != null && !registrationSuccess) {
            String endpoint = SCHEDULER_CONTROLLER_BASE_PATH + REGISTER_SERVICE;
            HttpEntity<RegisterServerInfo> httpEntity = new HttpEntity<>(registerServerInfo);

            Map<String, Boolean> serviceList = null;
            try {
                TigerITBillingServer tigerITBillingServer = new TigerITBillingServer();
                Object customersPayload = tigerITBillingServer.postData(endpoint, httpEntity);

                if(customersPayload instanceof Map){
                    serviceList = (Map<String, Boolean>) customersPayload;
                }
                else {
                    throw new ClassCastException(customersPayload.toString());
                }

            } catch (Exception e) {

                log.error(e.getMessage());
            }

            if (serviceList != null) {
                schedulerConfiguration.registerServices(serviceList);
                registrationSuccess = true;
            }
        }
    }

    private RegisterServerInfo ResolveServerInfo() {
        RegisterServerInfo registerServerInfo = null;

        if (customAppProperties != null) {
            registerServerInfo = new RegisterServerInfo();
            if (hostAddress == null) {
                try {
                    hostAddress = InetAddress.getLocalHost().getHostAddress();

                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }

            if (schedulerPort == null) schedulerPort = customAppProperties.getServerPort();

            registerServerInfo.setInstanceIPAddress(hostAddress);
            registerServerInfo.setPort(schedulerPort);
            registerServerInfo.setServiceName("scheduler-service");
            registerServerInfo.setEnabledSchedulerServices(getEnabledSchedulerServices());
        }

        return registerServerInfo;
    }

    public Date nextTryCron() {
        Date nextExec = null;
        if (!registrationSuccess) {
            Date currentDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);
            c.add(Calendar.SECOND, 15);

            nextExec = c.getTime();
            String dateString = DateConvertUtils.jDate_to_TimeStampString(nextExec);
            log.info("[ SCHEDULER ] Scheduler is initiating .......... will try after .. " + dateString);
        }
        return nextExec;
    }
}