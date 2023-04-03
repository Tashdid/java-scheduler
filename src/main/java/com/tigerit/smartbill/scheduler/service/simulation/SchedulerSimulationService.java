package com.tigerit.smartbill.scheduler.service.simulation;

import com.tigerit.smartbill.common.util.DateConvertUtils;
import com.tigerit.smartbill.common.util.DynamicGlobals;
import com.tigerit.smartbill.common.util.Utils;
import com.tigerit.smartbill.scheduler.config.simulation.SchedulerSimulationConfigValues;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static com.tigerit.smartbill.common.util.Constants.LOG_MSG;
import static com.tigerit.smartbill.common.values.ProjectWideConstants.SimulationConfiguration.ONE_ITERATION_DONE_SUCCESSFULLY;

@Slf4j
@Service
public class SchedulerSimulationService {

    SchedulerSimulationConfigValues schedulerSimulationConfigValues;
    SimulationCallback simulationCallback;


//    BillGenerationScheduler billGenerationScheduler;
//    @Autowired //see if cyclic //todo
//    public void setBillGenerationScheduler(BillGenerationScheduler billGenerationScheduler) {
//        this.billGenerationScheduler = billGenerationScheduler;
//    }

    @Qualifier("inMemorySchedulerSimulationConfigValues")
    @Autowired
    public void setSchedulerSimulationConfigValues(SchedulerSimulationConfigValues schedulerSimulationConfigValues) {
        this.schedulerSimulationConfigValues = schedulerSimulationConfigValues;
    }

    public void startSimulation() {

        //check: if currentDate <= endDate
        boolean shouldRunFirstIteration = false;
        try {
            if
            (
                    DateConvertUtils
                            .is_DDMMYYY_DateBeforeOrEqual(
                                    schedulerSimulationConfigValues.getSimulationStartDate(),
                                    schedulerSimulationConfigValues.getSimulationEndDate())
                            &&

                            DateConvertUtils
                                    .is_DDMMYYY_DateBeforeOrEqual(
                                            schedulerSimulationConfigValues.getSimulationCurrentDate(),
                                            schedulerSimulationConfigValues.getSimulationEndDate())

                            &&
                            DateConvertUtils
                                    .is_DDMMYYY_DateBeforeOrEqual(
                                            schedulerSimulationConfigValues.getSimulationStartDate(),
                                            schedulerSimulationConfigValues.getSimulationCurrentDate())

            ) {
                shouldRunFirstIteration = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (!shouldRunFirstIteration) {
                LOG_MSG = "!shouldRunFirstIteration";
                LOG_MSG += " Aborting/Skipping.";
                log.warn(Utils.prepareLogMessage(LOG_MSG));
                return;
            }
        }


        DynamicGlobals.shouldRunScheduler = false;

        simulationCallback = new SimulationCallback();
//        billGenerationScheduler.setSimulationCallback(simulationCallback);

        //initialize
        DynamicGlobals.useActualTodayValue = false;
        DynamicGlobals.todayString = schedulerSimulationConfigValues.getSimulationStartDate();

        schedulerSimulationConfigValues.setLastIterationSuccessful(true); //important: it needs to be set to false in appropriate place
        //eg: right after the successful BEGINNING of an iteration

        DynamicGlobals.shouldRunScheduler = true; //it is crucial that we set this at last?
    }

    public void runNextSimulationIteration() {
        DynamicGlobals.useActualTodayValue = false;
        DynamicGlobals.todayString = schedulerSimulationConfigValues.getSimulationCurrentDate();
        schedulerSimulationConfigValues.setLastIterationSuccessful(true); //important: it needs to be set to false in appropriate place
        //eg: right after the successful BEGINNING of an iteration
        DynamicGlobals.shouldRunScheduler = true; //it is crucial that we set this at last?
    }

    public void stopSimulation() {
        DynamicGlobals.shouldRunScheduler = false;
    }

    public class SimulationCallback {
        public void updateSimulation(String simulationStatus) {
            if (StringUtils.isBlank(simulationStatus)) {
                return;
            }

            if (simulationStatus.equals(ONE_ITERATION_DONE_SUCCESSFULLY)) {
                //check todo; whether should run the next iteration

                DynamicGlobals.shouldRunScheduler = false;
                DynamicGlobals.todayString = "";
                DynamicGlobals.shouldRunScheduler = true;

                boolean shouldRunNextIteration = false;

                try {
                    String newCurrentDay = DateConvertUtils.addOneDayToA_DDMMYYYY_String(
                            schedulerSimulationConfigValues.getSimulationCurrentDate()
                    );
                    if (DateConvertUtils.is_DDMMYYY_DateBeforeOrEqual(
                            newCurrentDay, schedulerSimulationConfigValues.getSimulationEndDate()
                    )) {
                        schedulerSimulationConfigValues.setSimulationCurrentDate(newCurrentDay);
                        shouldRunNextIteration = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    stopSimulation();
                } finally {

                    if (shouldRunNextIteration) {
                        runNextSimulationIteration();
                    } else {
                        stopSimulation();
                    }
                }
            } else {
                LOG_MSG = "'simulationStatus' unknown";
                log.warn(Utils.prepareLogMessage(LOG_MSG));
                stopSimulation();
            }
        }
    }
}
