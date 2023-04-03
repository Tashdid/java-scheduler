package com.tigerit.smartbill.scheduler.config;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SchedulerConfigValues {
    @NotBlank
    private String billGenerationCron;

    @NotBlank
    private String configDailyCron;

    @NotBlank
    private String configWeeklyCron;

    @NotBlank
    private String configMonthlyCron;

    @NotBlank
    private String pdfGenerationCron;

    @NotBlank
    private String dailyCommunicationCron;

    @NotBlank
    private String dailyCronJobIfFailed;

    @NotBlank
    private String weeklyCronJobIfFailed;

    @NotBlank
    private String monthlyCronJobIfFailed;

    @NotBlank
    private String dataArchiveCron;

    @NotBlank
    private String dummyTaskCron;

    @NotBlank
    private String dummyTaskCronFailed;
}
