package com.tigerit.smartbill.scheduler.service.monitoring;

import com.tigerit.smartbill.common.dto.RegisterServerInfo;
import com.tigerit.smartbill.common.model.dto.SchedulerStatus;
import com.tigerit.smartbill.scheduler.model.*;
import com.tigerit.smartbill.scheduler.service.network.TigerITBillingServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

import static com.tigerit.smartbill.common.values.APIValues.BillingServer.Scheduler.SCHEDULER_CONTROLLER_BASE_PATH;
import static com.tigerit.smartbill.common.values.APIValues.BillingServer.Scheduler.UNREGISTER_SERVICE;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SchedulerConfiguration {

    public static String SCHEDULER_DISABLE = "disable";
    public static String SCHEDULER_MANUAL = "manual";
    public static String SCHEDULER_AUTOMATIC = "automatic";

    public static String SCHEDULER_BILL_GENERATION = "Bill-Generation";
    public static String SCHEDULER_PREPAY_STATUS_CHECK = "PrePay-Status-Check";
    public static String SCHEDULER_PDF_GENERATION = "Pdf-Generation";
    public static String SCHEDULER_EMAIL_SMS_NOTIFICATION = "Email-SMS-Notification";
    public static String SCHEDULER_DAILY_TRANSACTION = "Daily-Transaction";
    public static String SCHEDULER_WEEKLY_TRANSACTION = "Weekly-Transaction";
    public static String SCHEDULER_MONTHLY_TRANSACTION = "Monthly-Transaction";
    public static String SCHEDULER_DATA_ARCHIVE = "Data-Archive";

    //TODO remove dummy-scheduler after testing completed !!!
    public static String SCHEDULER_DUMMY_TASK = "Dummy-Task";

    static Map<String, ScheduledFuture<?>> futureMap = new HashMap<>();
    static Map<String, ISchedulerInterface> interfaceMap = new HashMap<>();

    @Autowired
    ScheduleBillGeneration scheduleBillGeneration;
    @Autowired
    ScheduleDailyTransaction scheduleDailyTransaction;
    @Autowired
    ScheduleWeeklyTransaction scheduleWeeklyTransaction;
    @Autowired
    ScheduleMonthlyTransaction scheduleMonthlyTransaction;
    @Autowired
    SchedulePdfGeneration schedulePdfGeneration;
    @Autowired
    ScheduleDailyCommunication scheduleDailyCommunication;
    @Autowired
    ScheduleDataBackup scheduleDataBackup;
    @Autowired
    SchedulePrepayStatusCheck schedulePrepayStatusCheck;
    @Autowired
    DummySchedulerInstance dummySchedulerInstance;
    @Autowired
    ScheduleServerRegistration scheduleServerRegistration;

    @Autowired
    @Qualifier("reservationPool")
    ThreadPoolTaskScheduler taskScheduler;

    public static boolean test() {
        return false;
    }

    public static ISchedulerInterface getSchedulerInterface(String schedulerName) {
        ISchedulerInterface scheduledInterface = interfaceMap.get(schedulerName);
        return scheduledInterface;
    }

    /**
    @PostConstruct
    private void InitSchedulerMapInstance(){
        futureMap = SchedulerAppListener.hashMap;
        
        if (futureMap != null) {
        	if (futureMap.containsKey(SCHEDULER_BILL_GENERATION)) interfaceMap.put(SCHEDULER_BILL_GENERATION, scheduleBillGeneration);
        	if (futureMap.containsKey(SCHEDULER_PREPAY_STATUS_CHECK)) interfaceMap.put(SCHEDULER_PREPAY_STATUS_CHECK, schedulePrepayStatusCheck);
        	if (futureMap.containsKey(SCHEDULER_PDF_GENERATION)) interfaceMap.put(SCHEDULER_PDF_GENERATION, schedulePdfGeneration);
        	if (futureMap.containsKey(SCHEDULER_EMAIL_SMS_NOTIFICATION)) interfaceMap.put(SCHEDULER_EMAIL_SMS_NOTIFICATION, scheduleDailyCommunication);
        	if (futureMap.containsKey(SCHEDULER_DAILY_TRANSACTION)) interfaceMap.put(SCHEDULER_DAILY_TRANSACTION, scheduleDailyTransaction);
        	if (futureMap.containsKey(SCHEDULER_WEEKLY_TRANSACTION)) interfaceMap.put(SCHEDULER_WEEKLY_TRANSACTION, scheduleWeeklyTransaction);
        	if (futureMap.containsKey(SCHEDULER_MONTHLY_TRANSACTION)) interfaceMap.put(SCHEDULER_MONTHLY_TRANSACTION, scheduleMonthlyTransaction);
        	if (futureMap.containsKey(SCHEDULER_DATA_ARCHIVE)) interfaceMap.put(SCHEDULER_DATA_ARCHIVE, scheduleDataBackup);
        }
    }

    public void configure(){
        if (futureMap != null) {
        	if (futureMap.containsKey(SCHEDULER_BILL_GENERATION)) interfaceMap.put(SCHEDULER_BILL_GENERATION, scheduleBillGeneration);
        	if (futureMap.containsKey(SCHEDULER_PREPAY_STATUS_CHECK)) interfaceMap.put(SCHEDULER_PREPAY_STATUS_CHECK, schedulePrepayStatusCheck);
        	if (futureMap.containsKey(SCHEDULER_PDF_GENERATION)) interfaceMap.put(SCHEDULER_PDF_GENERATION, schedulePdfGeneration);
        	if (futureMap.containsKey(SCHEDULER_EMAIL_SMS_NOTIFICATION)) interfaceMap.put(SCHEDULER_EMAIL_SMS_NOTIFICATION, scheduleDailyCommunication);
        	if (futureMap.containsKey(SCHEDULER_DAILY_TRANSACTION)) interfaceMap.put(SCHEDULER_DAILY_TRANSACTION, scheduleDailyTransaction);
        	if (futureMap.containsKey(SCHEDULER_WEEKLY_TRANSACTION)) interfaceMap.put(SCHEDULER_WEEKLY_TRANSACTION, scheduleWeeklyTransaction);
        	if (futureMap.containsKey(SCHEDULER_MONTHLY_TRANSACTION)) interfaceMap.put(SCHEDULER_MONTHLY_TRANSACTION, scheduleMonthlyTransaction);
        	if (futureMap.containsKey(SCHEDULER_DATA_ARCHIVE)) interfaceMap.put(SCHEDULER_DATA_ARCHIVE, scheduleDataBackup);
        	
        	ConfigurableSchedulerTask.taskScheduler.schedule(scheduleDailyCommunication.getRunnable(), scheduleDailyCommunication.getTrigger());
        }
    }
     */

    public static boolean isSchedulerListed(String schedulerName) {
        Set<String> schedulerNameSet = futureMap.keySet();
        return schedulerNameSet.contains(schedulerName);
    }

    public static boolean checkSchedulerStatus(String schedulerName) {
        ScheduledFuture<?> scheduledFuture = futureMap.get(schedulerName);
        return scheduledFuture != null;
    }

    public static ScheduledFuture<?> getSchedulerTask(String schedulerName) {
        ScheduledFuture<?> scheduledFuture = futureMap.get(schedulerName);
        return scheduledFuture;
    }

    public static boolean registerSchedulerName(String schedulerName, ScheduledFuture<?> scheduledFuture) {
        if (checkSchedulerStatus(schedulerName)) return true;
        try {
            futureMap.replace(schedulerName, scheduledFuture);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean cancelSchedulerName(String schedulerName) {
        if (!checkSchedulerStatus(schedulerName)) return true;

        try {
            futureMap.replace(schedulerName, null);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public ScheduledFuture<?> checkAndStartScheduler(String scheduleName, ThreadPoolTaskScheduler taskScheduler) {
        ISchedulerInterface schedulerInterface = getSchedulerInterface(scheduleName);
        if (schedulerInterface == null) return null;

        return taskScheduler.schedule(schedulerInterface.getRunnable(), schedulerInterface.getTrigger());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        ConfigurableSchedulerTask.taskScheduler.schedule(
                scheduleServerRegistration.getRunnable(), scheduleServerRegistration.getTrigger());
    }

    @PreDestroy
    public void onDestroy() throws Exception {
        RegisterServerInfo registerServerInfo = new RegisterServerInfo();
        registerServerInfo.setInstanceIPAddress(ServerRegistrationScheduler.hostAddress);
        registerServerInfo.setPort(ServerRegistrationScheduler.schedulerPort);

        String endpoint = SCHEDULER_CONTROLLER_BASE_PATH + UNREGISTER_SERVICE;
        HttpEntity<RegisterServerInfo> httpEntity = new HttpEntity<>(registerServerInfo);

        TigerITBillingServer tigerITBillingServer = new TigerITBillingServer();
        tigerITBillingServer.postData(endpoint, httpEntity);
    }

    public void registerServices(Map<String, Boolean> serviceList) {

        if (futureMap != null) {
            serviceList.forEach((key, status) -> {

                futureMap.put(key, null);
            });
        }
        InitSchedulerMapInstance(serviceList);
    }

    private void InitSchedulerMapInstance(Map<String, Boolean> serviceList) {
        if (futureMap != null) {
            if (futureMap.containsKey(SCHEDULER_BILL_GENERATION))
                interfaceMap.put(SCHEDULER_BILL_GENERATION, scheduleBillGeneration);
            if (futureMap.containsKey(SCHEDULER_PREPAY_STATUS_CHECK))
                interfaceMap.put(SCHEDULER_PREPAY_STATUS_CHECK, schedulePrepayStatusCheck);
            if (futureMap.containsKey(SCHEDULER_PDF_GENERATION))
                interfaceMap.put(SCHEDULER_PDF_GENERATION, schedulePdfGeneration);
            if (futureMap.containsKey(SCHEDULER_EMAIL_SMS_NOTIFICATION))
                interfaceMap.put(SCHEDULER_EMAIL_SMS_NOTIFICATION, scheduleDailyCommunication);
            if (futureMap.containsKey(SCHEDULER_DAILY_TRANSACTION))
                interfaceMap.put(SCHEDULER_DAILY_TRANSACTION, scheduleDailyTransaction);
            if (futureMap.containsKey(SCHEDULER_WEEKLY_TRANSACTION))
                interfaceMap.put(SCHEDULER_WEEKLY_TRANSACTION, scheduleWeeklyTransaction);
            if (futureMap.containsKey(SCHEDULER_MONTHLY_TRANSACTION))
                interfaceMap.put(SCHEDULER_MONTHLY_TRANSACTION, scheduleMonthlyTransaction);
            if (futureMap.containsKey(SCHEDULER_DATA_ARCHIVE))
                interfaceMap.put(SCHEDULER_DATA_ARCHIVE, scheduleDataBackup);
            if (futureMap.containsKey(SCHEDULER_DUMMY_TASK))
                interfaceMap.put(SCHEDULER_DUMMY_TASK, dummySchedulerInstance);
        }

        /**
         * start schedulers that are configured enable
         */
        //TODO edge case handle requires if service start fails , db log is done prior to service start !!!!
        serviceList.forEach((key, status) -> {
            if (status == true) {
                ScheduledFuture<?> scheduledFuture = checkAndStartScheduler(key, this.taskScheduler);
                registerSchedulerName(key, scheduledFuture);
            }
        });
    }


    public List<SchedulerStatus> getSchedulerStatusFromStartupInfo() {
        List<SchedulerStatus> schedulerStatusList = new ArrayList<>();
        if (futureMap != null) {
            Set<String> schedulerNameSet = futureMap.keySet();
            schedulerNameSet.forEach(
                    schedulerName -> {
                        SchedulerStatus scheduler = new SchedulerStatus();
                        scheduler.setSchedulerName(schedulerName);
                        boolean isRunning = checkSchedulerStatus(schedulerName);
                        scheduler.setRunning(isRunning);
                        schedulerStatusList.add(scheduler);
                    });
        }
        return schedulerStatusList;
    }
}
