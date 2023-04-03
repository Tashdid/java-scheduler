package com.tigerit.smartbill.scheduler.controller;

import com.tigerit.smartbill.common.model.api.response.common.GenericAPIResponse;
import com.tigerit.smartbill.common.model.dto.SchedulerStatus;
import com.tigerit.smartbill.common.util.DynamicGlobals;
import com.tigerit.smartbill.common.util.Utils;
import com.tigerit.smartbill.scheduler.config.security.access.IsSuperAdmin;
import com.tigerit.smartbill.scheduler.config.simulation.SchedulerSimulationConfigValues;
import com.tigerit.smartbill.scheduler.model.ISchedulerInterface;
import com.tigerit.smartbill.scheduler.service.monitoring.SchedulerConfiguration;
import com.tigerit.smartbill.scheduler.service.simulation.SchedulerSimulationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import static com.tigerit.smartbill.common.model.util.APIExposedPojoHelper.makeSuccessfulResponseWithThisData;
import static com.tigerit.smartbill.common.util.Constants.LOG_MSG;
import static com.tigerit.smartbill.common.values.APIValues.SchedulerServer.SchedulerConfigController.SchedulerConfigController_BASE_PATH;

@IsSuperAdmin
@Slf4j
@RestController
@RequestMapping(
        SchedulerConfigController_BASE_PATH
)
//http://localhost:8083/scheduler/api/v1/config
public class SchedulerConfigController {

    //	@Autowired
//	ConfigurableSchedulerTask configurableSchedulerTask;

    SchedulerSimulationConfigValues schedulerSimulationConfigValues;
    SchedulerSimulationService schedulerSimulationService;
    @Autowired
    @Qualifier("reservationPool")
    ThreadPoolTaskScheduler taskScheduler;
    /*@Autowired
    ScheduleBillGeneration scheduleBillGeneration;
    @Autowired
    ScheduleDailyTransaction scheduleDailyTransaction;
    //@Autowired
    //BillGenerationSchedulerThread billGenerationSchedulerThread;
    @Autowired
    ScheduleWeeklyTransaction scheduleWeeklyTransaction;
    @Autowired
    ScheduleMonthlyTransaction scheduleMonthlyTransaction;
    @Autowired
    SchedulePdfGeneration schedulePdfGeneration;
    @Autowired
    ScheduleDailyCommunication scheduleDailyCommunication;
    @Autowired
    ScheduleDataBackup scheduleDataBackup;*/

    @Autowired
    SchedulerConfiguration schedulerConfiguration;

    @Qualifier("inMemorySchedulerSimulationConfigValues")
    @Autowired
    public void setSchedulerSimulationConfigValues(SchedulerSimulationConfigValues schedulerSimulationConfigValues) {
        this.schedulerSimulationConfigValues = schedulerSimulationConfigValues;
    }

    @Autowired
    public void setSchedulerSimulationService(SchedulerSimulationService schedulerSimulationService) {
        this.schedulerSimulationService = schedulerSimulationService;
    }


    @GetMapping("/start_scheduler")
    public ResponseEntity<GenericAPIResponse> startScheduler(@RequestParam String scheduleName) {
        //DynamicGlobals.shouldRunScheduler = true;
        if (!SchedulerConfiguration.isSchedulerListed(scheduleName)) {
            String msg = "Scheduler [" + scheduleName + "] is not listed";
            log.info(msg);
            return ResponseEntity.ok(makeSuccessfulResponseWithThisData(msg));
        }

//        ScheduledFuture<?> scheduledFuture = checkAndStartScheduler(scheduleName);
        ScheduledFuture<?> scheduledFuture = schedulerConfiguration.checkAndStartScheduler(scheduleName,this.taskScheduler);
        if (scheduledFuture == null) {
            String msg = "Scheduler [" + scheduleName + "] is not avilable";
            log.info(msg);
            return ResponseEntity.ok(makeSuccessfulResponseWithThisData(msg));
        }

        SchedulerConfiguration.registerSchedulerName(scheduleName, scheduledFuture);

        String msg = "Scheduler [" + scheduleName + "] running";
        log.info(msg);
        return ResponseEntity.ok(makeSuccessfulResponseWithThisData(msg));
    }


    @GetMapping("/stop_scheduler")
    public ResponseEntity<GenericAPIResponse> stopScheduler(@RequestParam String scheduleName) {

        /*
        SchedulerList schedulerInstance = billingServerRemoteAPI.getSchedulerName(scheduleName);
        if (!schedulerInstance.getIpAddress().equals(metadata.getRemoteAddress())) {
            String msg = "Scheduler [" + scheduleName + "] is not stoped, request initiated from different IP address";
            log.info(msg);
            return ResponseEntity.ok(makeSuccessfulResponseWithThisData(msg));
        }
         */
        //DynamicGlobals.shouldRunScheduler = false;
        if (!SchedulerConfiguration.isSchedulerListed(scheduleName)) {
            String msg = "Scheduler [" + scheduleName + "] is not listed";
            log.info(msg);
            return ResponseEntity.ok(makeSuccessfulResponseWithThisData(msg));
        }

        if (!SchedulerConfiguration.checkSchedulerStatus(scheduleName)) {
            String msg = "Scheduler [" + scheduleName + "] is already stopped";
            log.info(msg);
            return ResponseEntity.ok(makeSuccessfulResponseWithThisData(msg));
        }

        ScheduledFuture<?> scheduledFuture = SchedulerConfiguration.getSchedulerTask(scheduleName);
        if (scheduledFuture != null) scheduledFuture.cancel(true);

        SchedulerConfiguration.cancelSchedulerName(scheduleName);

        String msg = "Scheduler [" + scheduleName + "] stopped";
        log.info(msg);
        return ResponseEntity.ok(makeSuccessfulResponseWithThisData(msg));
    }

    @GetMapping("/status")
    public ResponseEntity<GenericAPIResponse> status() {

        List<SchedulerStatus> schedulerStatusList = schedulerConfiguration.getSchedulerStatusFromStartupInfo();
        return ResponseEntity.ok(makeSuccessfulResponseWithThisData(schedulerStatusList));
    }


    //#region [Simulation]
    @GetMapping("/simulation")
    public SchedulerSimulationConfigValues getSimulationConfig() {

        if (schedulerSimulationConfigValues == null) {
            LOG_MSG = "schedulerSimulationConfigValues == null";
            log.warn(Utils.prepareLogMessage(LOG_MSG));

            schedulerSimulationConfigValues = new SchedulerSimulationConfigValues();
        }

        return schedulerSimulationConfigValues;
    }

    @PostMapping("/simulation")
    public SchedulerSimulationConfigValues configAndStartSimulation(@Validated @RequestBody SchedulerSimulationConfigValues request) {

        boolean ok = true;
        if (StringUtils.isBlank(request.getSimulationStartDate())) {
            ok = false;
        }
        if (!Utils.isDateValid(request.getSimulationStartDate())) {
            ok = false;
        }
        if (StringUtils.isBlank(request.getSimulationEndDate())) {
            ok = false;
        }
        if (!Utils.isDateValid(request.getSimulationEndDate())) {
            ok = false;
        }
        //add more checks if needed
        ///eg: add chronology check //todo
        if (ok) {
            schedulerSimulationConfigValues.setSimulationStartDate(request.getSimulationStartDate());
            schedulerSimulationConfigValues.setSimulationEndDate(request.getSimulationEndDate());
            schedulerSimulationConfigValues.setSimulationCurrentDate(request.getSimulationStartDate());
            schedulerSimulationConfigValues.setLastIterationSuccessful(false); //it will be set to true right before the first iteration

            //call service layer
            schedulerSimulationService.startSimulation();
        }

        return schedulerSimulationConfigValues;
    }
    //#endregion [Simulation]

    @GetMapping("/set_today_value")
    public String setTodayValue(@RequestParam("today") String todayValue) {
        DynamicGlobals.useActualTodayValue = false;
        DynamicGlobals.todayString = todayValue;
        return "value set successfully: today = " + todayValue;
        //return prepareStatus();
    }

    @GetMapping("/set_actual_today_value")
    public String setActualTodayValue() {

        String actualTodayValue = ""; //eg: format: 3/5/2019
        Date date = new Date();
        actualTodayValue = Utils.getDayStringFromJavaDate(date);
        DynamicGlobals.todayString = actualTodayValue;

        DynamicGlobals.useActualTodayValue = true;
        return "Actual today value set successfully: today = " + DynamicGlobals.todayString;
    }

    @GetMapping("/use_actual_today_value")
    public String useActualTodayValue() {
        DynamicGlobals.useActualTodayValue = true;

        String actualTodayValue = ""; //eg: format: 3/5/2019
        Date date = new Date();
        actualTodayValue = Utils.getDayStringFromJavaDate(date);
        DynamicGlobals.todayString = actualTodayValue;

        return "Scheduler will now use actual today value";
    }

    @GetMapping("/test")
    public String test() {
        return "test successful";
    }

    /*private SchedulerStatus prepareStatus() {
        SchedulerStatus schedulerStatus = new SchedulerStatus();

        schedulerStatus.setRunning(DynamicGlobals.shouldRunScheduler);
        schedulerStatus.setTodayValue(DynamicGlobals.todayString);
        schedulerStatus.setUseActualTodayValue(DynamicGlobals.useActualTodayValue);
        schedulerStatus.setTimeIntervalInMilliSecond(DynamicGlobals.timeIntervalInMilliSeconds);
        //add more

        return schedulerStatus;
    }*/

    private ScheduledFuture<?> checkAndStartScheduler(String scheduleName) {
        ISchedulerInterface schedulerInterface = getSchedulerInterface(scheduleName);
        if (schedulerInterface == null) return null;

        return this.taskScheduler.schedule(schedulerInterface.getRunnable(), schedulerInterface.getTrigger());
    }



    private ISchedulerInterface getSchedulerInterface(String scheduleName) {
        return SchedulerConfiguration.getSchedulerInterface(scheduleName);
    }

}
