package com.tigerit.smartbill.scheduler.config;

import com.tigerit.smartbill.scheduler.config.simulation.SchedulerSimulationConfigValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.tigerit.smartbill.common.values.ProjectWideConstants.SimulationConfiguration.SIMULATION_END_DATE;
import static com.tigerit.smartbill.common.values.ProjectWideConstants.SimulationConfiguration.SIMULATION_START_DATE;

@Configuration
public class SchedulerAppConfig {
    //<minute> <hour> <day-of-month> <month> <day-of-week> <command>
    @Value("${billGenerationCron}")
    private String billGenerationCron;

    @Value("${dailyCronJob}")
    private String dailyCron;

    @Value("${weeklyCronJob}")
    private String weeklyCron;

    @Value("${monthlyCronJob}")
    private String monthlyCron;

    @Value("${pdfGenerationCron}")
    private String pdfGenerationCron;

    @Value("${dailyCommunicationCron}")
    private String dailyCommunicationCron;

    @Value("${dailyCronJobIfFailed}")
    private String dailyCronJobIfFailed;

    @Value("${weeklyCronJobIfFailed}")
    private String weeklyCronJobIfFailed;

    @Value("${monthlyCronJobIfFailed}")
    private String monthlyCronJobIfFailed;

    @Value("${dataArchiveCron}")
    private String dataArchiveCron;

    @Value("${dummyTaskCronJob}")
    private String dummyTaskCron;

    @Value("${dummyTaskCronJobIfFailed}")
    private String dummyTaskCronFailed;

    @Bean(name = "inMemorySchedulerConfigValues")
    public SchedulerConfigValues getSchedulerConfigValues() {
        SchedulerConfigValues schedulerConfigValues = new SchedulerConfigValues();

        schedulerConfigValues.setBillGenerationCron(billGenerationCron);
        schedulerConfigValues.setConfigDailyCron(dailyCron);
        schedulerConfigValues.setConfigWeeklyCron(weeklyCron);
        schedulerConfigValues.setConfigMonthlyCron(monthlyCron);
        schedulerConfigValues.setPdfGenerationCron(pdfGenerationCron);
        schedulerConfigValues.setDailyCommunicationCron(dailyCommunicationCron);
        schedulerConfigValues.setDailyCronJobIfFailed(dailyCronJobIfFailed);
        schedulerConfigValues.setWeeklyCronJobIfFailed(weeklyCronJobIfFailed);
        schedulerConfigValues.setMonthlyCronJobIfFailed(monthlyCronJobIfFailed);
        schedulerConfigValues.setDataArchiveCron(dataArchiveCron);
        /*
        add dummy task cron for testing purpose
         */
        schedulerConfigValues.setDummyTaskCron(dummyTaskCron);
        schedulerConfigValues.setDummyTaskCronFailed(dummyTaskCronFailed);

        return schedulerConfigValues;
    }

    @Bean(name = "inMemorySchedulerSimulationConfigValues")
    public SchedulerSimulationConfigValues getSchedulerSimulationConfigValues() {
        SchedulerSimulationConfigValues schedulerSimulationConfigValues = new SchedulerSimulationConfigValues();

        schedulerSimulationConfigValues.setSimulationStartDate(SIMULATION_START_DATE);
        schedulerSimulationConfigValues.setSimulationEndDate(SIMULATION_END_DATE);
        schedulerSimulationConfigValues.setSimulationCurrentDate(SIMULATION_START_DATE);
        //DevMU
        schedulerSimulationConfigValues.setLastIterationSuccessful(false); //it will be set to true just before starting a simulation
        //todo:
        //note: reasoning?
        //DevTJ
        //schedulerSimulationConfigValues.setLastIterationSuccessful(true);

        return schedulerSimulationConfigValues;
    }
}
