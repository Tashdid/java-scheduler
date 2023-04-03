package com.tigerit.smartbill.scheduler.config.simulation;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SchedulerSimulationConfigValues {
    //the below 2 fields are taken as input from REST call
    @NotBlank
//    @DateTimeFormat
    private String simulationStartDate;

    @NotBlank
    private String simulationEndDate;

    //the below 3 values will be updated upon each iteration
    private String simulationCurrentDate;
    private boolean lastIterationSuccessful;
    //private boolean shouldRunSimulation;
}
