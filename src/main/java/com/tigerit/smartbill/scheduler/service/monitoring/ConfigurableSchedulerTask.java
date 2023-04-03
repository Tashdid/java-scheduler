package com.tigerit.smartbill.scheduler.service.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class ConfigurableSchedulerTask implements SchedulingConfigurer {
    static ThreadPoolTaskScheduler taskScheduler = null;

    //BillGenerationScheduler billGenerationScheduler;
    //PayableBillingRecordNotifierScheduler communicationScheduler;

    //Ataur
    //ScheduledTaskRegistrar scheduledTaskRegistrar;

    //<minute> <hour> <day-of-month> <month> <day-of-week> <command>
    //@Value("${dailyCronJob}")
    //private String dailyCron;

    //@Value("${weeklyCronJob}")
    //private String weeklyCron;// = "0 * * * * ? ";

    //@Value("${monthlyCronJob}")
    //private String monthlyCron; //= "0 * * * * ? ";

    //@Value("${pdfGenerationCron}")
    //private String dailyPDFCron; //= "* * * * * ? ";

    //@Value("${dailyCommunicationCron}")
    //private String dailyCommunicationCron; //= "0 * * * * ? ";
    @Autowired
    Environment env;
    //@Autowired
    //public void setBillGenerationScheduler(BillGenerationScheduler billGenerationScheduler) {
    //    this.billGenerationScheduler = billGenerationScheduler;
    //}

    //BillGenerationSchedulerThread billGenerationSchedulerThread;

    //@Autowired
    //public void setBillGenerationSchedulerThread(BillGenerationSchedulerThread billGenerationSchedulerThread) {
    //    this.billGenerationSchedulerThread = billGenerationSchedulerThread;
    //}

    //@Autowired
    //public void setCommunicationScheduler(PayableBillingRecordNotifierScheduler communicationScheduler) {
    //    this.communicationScheduler = communicationScheduler;
    //}

    /*@Bean
    public BillGenerationScheduler billGenerationScheduler() {
        return new BillGenerationScheduler();
    }*/

    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(150);
    }

    @Bean
    ThreadPoolTaskScheduler reservationPool() {
        return new ThreadPoolTaskScheduler();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //SchedulerConfiguration.test();

        if (taskScheduler == null) {
            taskScheduler = reservationPool();
            //taskRegistrar.setScheduler (taskExecutor ());
            taskRegistrar.setScheduler(taskScheduler);
        }

        //ScheduleServerRegistration scheduleServerRegistration = new ScheduleServerRegistration();
        //taskScheduler.schedule(scheduleServerRegistration.getRunnable(), scheduleServerRegistration.getTrigger());
    	
        /*taskRegistrar.addTriggerTask (
                new Runnable () {
                    @Override
                    public void run() {
                        //billGenerationScheduler.executeScheduler();
                        billGenerationSchedulerThread.executeScheduler ();

                    }
                },
                new Trigger () {
                    @Override
                    public Date nextExecutionTime(TriggerContext triggerContext) {
                        Calendar nextExecutionTime = new GregorianCalendar ();
                        Date lastActualExecutionTime = triggerContext.lastActualExecutionTime ();
                        nextExecutionTime.setTime (lastActualExecutionTime != null ? lastActualExecutionTime : new Date ());
                        int intTimeIntervalInMilliSeconds = (int) DynamicGlobals.timeIntervalInMilliSeconds;
                        nextExecutionTime.add (Calendar.MILLISECOND, intTimeIntervalInMilliSeconds); //you can get the value from wherever you want

                        Date date = nextExecutionTime.getTime ();
                        String dateString = DateConvertUtils.jDate_to_TimeStampString (date);
                        System.out.println ("[ATAUR]billGenerationSchedulerThread..........next call " + dateString);

                        return nextExecutionTime.getTime ();
                    }
                }
        );

        taskRegistrar.addTriggerTask (
                () -> testScheduler.DailyReportGenerate (),
                triggerContext -> {
                    Calendar nextExecutionTime = new GregorianCalendar ();
                    Date lastActualExecutionTime = triggerContext.lastActualExecutionTime ();
                    nextExecutionTime.setTime (lastActualExecutionTime != null ? lastActualExecutionTime : new Date ());
                    int intTimeIntervalInMilliSeconds = (int) DynamicGlobals.timeIntervalInMilliSeconds; //testTimeIntervalInMilliSeconds;
                    nextExecutionTime.add (Calendar.MILLISECOND, intTimeIntervalInMilliSeconds); //you can get the value from wherever you want

                    Date date = nextExecutionTime.getTime ();
                    String dateString = DateConvertUtils.jDate_to_TimeStampString (date);
                    System.out.println ("[Tashdid] Report Generation..........next call " + dateString);

                    return nextExecutionTime.getTime ();
                }
        );
        taskRegistrar.addCronTask (
                () -> testScheduler.WeeklyReportGenerate (),
                weeklyCron
        );
        taskRegistrar.addCronTask (
                () -> testScheduler.MonthlyReportGenerate (),
                monthlyCron
        );*/
//
//        taskRegistrar.addCronTask (
//                () -> communicationScheduler.InitEmailAnsSMSSendScheduler (), dailyCommunicationCron
//        );
//        taskRegistrar.addCronTask (
//                () -> communicationScheduler.generatePDFScheduler (), dailyPDFCron
//        );

    }
    
    /*
    public void addScheduledTask() {
    	scheduledTaskRegistrar.addTriggerTask(
            new Runnable () {
                @Override
                public void run() {
                    //billGenerationScheduler.executeScheduler();
                    billGenerationSchedulerThread.executeScheduler ();

                }
            },
            new Trigger () {
                @Override
                public Date nextExecutionTime(TriggerContext triggerContext) {
                    Calendar nextExecutionTime = new GregorianCalendar ();
                    Date lastActualExecutionTime = triggerContext.lastActualExecutionTime ();
                    nextExecutionTime.setTime (lastActualExecutionTime != null ? lastActualExecutionTime : new Date ());
                    int intTimeIntervalInMilliSeconds = (int) DynamicGlobals.timeIntervalInMilliSeconds;
                    nextExecutionTime.add (Calendar.MILLISECOND, intTimeIntervalInMilliSeconds); //you can get the value from wherever you want

                    Date date = nextExecutionTime.getTime ();
                    String dateString = DateConvertUtils.jDate_to_TimeStampString (date);
                    System.out.println ("[ATAUR]billGenerationSchedulerThread..........next call " + dateString);

                    return nextExecutionTime.getTime ();
                }
            }
    	);
	}

    public boolean addJob(String jobName) {
        if (futureMap.containsKey(jobName)) {
            return false;
        }

        ScheduledFuture future = scheduledTaskRegistrar.getScheduler().schedule(() -> methodToBeExecuted(), t -> {
            Calendar nextExecutionTime = new GregorianCalendar();
            Date lastActualExecutionTime = t.lastActualExecutionTime();
            nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
            nextExecutionTime.add(Calendar.SECOND, 5);
            return nextExecutionTime.getTime();
        });

        configureTasks(scheduledTaskRegistrar);
        futureMap.put(jobName, future);
        return true;
    }

    public boolean removeJob(String name) {
        if (!futureMap.containsKey(name)) {
            return false;
        }
        ScheduledFuture future = futureMap.get(name);
        future.cancel(true);
        futureMap.remove(name);
        return true;
    }

    public void methodToBeExecuted() {
        //LOGGER.info("methodToBeExecuted: Next execution time of this will always be 5 seconds");
    }
    */
}
