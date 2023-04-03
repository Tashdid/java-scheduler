//package com.tigerit.smartbill.scheduler.controller;
//
//import lombok.extern.slf4j.Slf4j;
////import org.apache.logging.log4j.Level;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
////import org.apache.logging.log4j.core.appender.ConsoleAppender;
////import org.apache.logging.log4j.core.config.Configuration;
////import org.apache.logging.log4j.core.config.ConfigurationFactory;
////import org.apache.logging.log4j.core.config.ConfigurationSource;
////import org.apache.logging.log4j.core.config.LoggerConfig;
////import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;
////import org.apache.logging.log4j.core.layout.PatternLayout;
////import org.apache.logging.log4j.spi.LoggerContext;
////import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.web.bind.annotation.GetMapping;
////import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
////import java.io.File;
////import java.io.FileInputStream;
//
//import static com.tigerit.smartbill.common.util.Constants.LOG_MSG;
//import static com.tigerit.smartbill.common.util.Constants.LOG_PREFIX;
//
//@Slf4j
//@RestController
//public class LogConfigurationController {
//
//    @GetMapping("/test/log")
//    public String testLogLevel(@RequestParam(value = "message", required = false, defaultValue = "SAMPLE LOG MSG FOR TESTING") String message) {
//        LOG_MSG = LOG_PREFIX + "See log output message :" + message;
//
//        log.trace(LOG_MSG);
//        log.debug(LOG_MSG);
//        log.info(LOG_MSG);
//        log.warn(LOG_MSG);
//        log.error(LOG_MSG);
//
//        return LOG_MSG;
//    }
//
//    @Bean
//    public CommandLineRunner run(){
//        return args -> {
//
//            //Test if log4j2 is configured
//            testLog4j2Configuration();
//
//            printSomeLogMessagesWithDifferentLevels();
//
//            //TMP
//            //Do programmatic configuration for log4j2
//            //configureLog4j2Programmatically();
//
//
//
//        };
//    }
//
//    private void printSomeLogMessagesWithDifferentLevels() {
//        //Printing some log message with different levels to verify proper logging configuration
//        LOG_MSG = LOG_PREFIX + "See log output message :" + "[THIS IS A SAMPLE LOG MESSAGE]";
//
//        String LOG_LEVEL_MSG_PREFIX = " [This log message's level : ] ";
//        String LOG_LEVEL = "";
//        String LOG_LEVEL_MSG = ""; //LOG_LEVEL_MSG_PREFIX + LOG_LEVEL;
//
//        //TRACE
//        LOG_LEVEL = "TRACE";
//        LOG_LEVEL_MSG = LOG_LEVEL_MSG_PREFIX + LOG_LEVEL;
//        log.trace(LOG_MSG + LOG_LEVEL_MSG);
//
//        //DEBUG
//        LOG_LEVEL = "DEBUG";
//        LOG_LEVEL_MSG = LOG_LEVEL_MSG_PREFIX + LOG_LEVEL;
//        log.debug(LOG_MSG + LOG_LEVEL_MSG);
//
//        //INFO
//        LOG_LEVEL = "INFO";
//        LOG_LEVEL_MSG = LOG_LEVEL_MSG_PREFIX + LOG_LEVEL;
//        log.info(LOG_MSG + LOG_LEVEL_MSG);
//
//        //WARN
//        LOG_LEVEL = "WARN";
//        LOG_LEVEL_MSG = LOG_LEVEL_MSG_PREFIX + LOG_LEVEL;
//        log.warn(LOG_MSG + LOG_LEVEL_MSG);
//
//        //ERROR
//        LOG_LEVEL = "ERROR";
//        LOG_LEVEL_MSG = LOG_LEVEL_MSG_PREFIX + LOG_LEVEL;
//        log.error(LOG_MSG + LOG_LEVEL_MSG);
//
//
//        //TMP
//        String osSystemUserDir = System.getProperty("user.dir");
//        LOG_MSG = LOG_PREFIX + "System.getProperty(\"user.dir\") : " + osSystemUserDir;
//        LOG_LEVEL = "ERROR";
//        LOG_LEVEL_MSG = LOG_LEVEL_MSG_PREFIX + LOG_LEVEL;
//        log.error(LOG_MSG + LOG_LEVEL_MSG);
//
//        //TMP
//        String osSystemUserHome = System.getProperty("user.home");
//        LOG_MSG = LOG_PREFIX + "System.getProperty(\"user.home\") : " + osSystemUserHome;
//        LOG_LEVEL = "ERROR";
//        LOG_LEVEL_MSG = LOG_LEVEL_MSG_PREFIX + LOG_LEVEL;
//        log.error(LOG_MSG + LOG_LEVEL_MSG);
//
//        //TMP
//        String osSystemUserName = System.getProperty("user.name");
//        LOG_MSG = LOG_PREFIX + "System.getProperty(\"user.name\") : " + osSystemUserName;
//        LOG_LEVEL = "ERROR";
//        LOG_LEVEL_MSG = LOG_LEVEL_MSG_PREFIX + LOG_LEVEL;
//        log.error(LOG_MSG + LOG_LEVEL_MSG);
//    }
///*
//    private void configureLog4j2Programmatically() {
//        try {
//            // Get instance of configuration factory; your options are default ConfigurationFactory, XMLConfigurationFactory,
//            // 	YamlConfigurationFactory & JsonConfigurationFactory
//            ConfigurationFactory factory =  XmlConfigurationFactory.getInstance();
//
//            // Locate the source of this configuration, this located file is dummy file contains just an empty configuration Tag
//            ConfigurationSource configurationSource = new ConfigurationSource(new FileInputStream(new File("C:/dummyConfiguration.xml")));
//
//            // Get a reference from configuration
//            Configuration configuration = factory.getConfiguration(configurationSource);
//
//            // Create default console appender
//            ConsoleAppender appender = ConsoleAppender.createDefaultAppenderForLayout(PatternLayout.createDefaultLayout());
//
//            // Add console appender into configuration
//            configuration.addAppender(appender);
//
//            // Create loggerConfig
//            LoggerConfig loggerConfig = new LoggerConfig("com", Level.FATAL,false);
//
//            // Add appender
//            loggerConfig.addAppender(appender,null,null);
//
//            // Add logger and associate it with loggerConfig instance
//            configuration.addLogger("com", loggerConfig);
//
//            // Get context instance
//            LoggerContext context = new LoggerContext("JournalDevLoggerContext");
//
//            // Start logging system
//            context.start(configuration);
//
//            // Get a reference for logger
//            Logger logger = context.getLogger("com");
//
//            // LogEvent of DEBUG message
//            logger.log(Level.FATAL, "Logger Name :: "+logger.getName()+" :: Passed Message ::");
//
//            // LogEvent of Error message for Logger configured as FATAL
//            logger.log(Level.ERROR, "Logger Name :: "+logger.getName()+" :: Not Passed Message ::");
//
//            // LogEvent of ERROR message that would be handled by Root
//            logger.getParent().log(Level.ERROR, "Root Logger :: Passed Message As Root Is Configured For ERROR Level messages");
//        }
//        catch (Exception e) {
//
//        }
//    }*/
//
//
//    private void testLog4j2Configuration() {
//        Logger logger_Log4j2 = LogManager.getRootLogger();
//        logger_Log4j2.error("Configuration File Defined To Be :: " + System.getProperty("log4j2.configurationFile"));
//    }
//}
