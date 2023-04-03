package com.tigerit.smartbill.scheduler.config.props;

import com.tigerit.smartbill.common.config.encrypt.EncryptDecryptPwd;
import com.tigerit.smartbill.common.util.Utils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Properties;

import static com.tigerit.smartbill.common.util.Constants.LOG_MSG;
import static com.tigerit.smartbill.common.values.ProjectWideConstants.CustomExternalConfiguration.SchedulerConfigs.*;

@Slf4j
@Data
//@Component
public class CustomAppProperties /*implements ICustomProperties*/ {
    Map<String, Properties> mp;

    //#region    [custom app properties]
    String serverPort;
    String databaseServer;
    String databasePort;
    String userPassword;
//    String backupServerApiUserPassword;

    String billingServer;
    String billingPort;
    String billingUsername;
    String billingPassword;

    String producerServer;
    String producerPort;
    String producerUsername;
    String producerPassword;

    String communicationServer;
    String communicationPort;
    String communicationUsername;
    String communicationPassword;

    String backupServer;
    String backupPort;
    String backupUsername;
    String backupPassword;

    String batchThreadNo;
    String batchFetchRow;

    /*
    scheduler names mapped from external config file
     */
    String billGeneration;
    String prepayStatusCheck;
    String pdfGeneration;
    String emailSmsNotification;
    String dailyTransaction;
    String weeklyTransaction;
    String monthlyTransaction;
    String dataArchive;
    String dummyTask;
    //#endregion [custom app properties]

    public String getDatabaseServerWithPort() {
        return this.databaseServer + ":" + databasePort;
    }

    public String getBillingServerWithPort() {
        return this.billingServer + ":" + this.billingPort;
    }

    public String getProducerServerWithPort() {
        return this.producerServer + ":" + this.producerPort;
    }

    public String getCommunicationServerWithPort() {
        return this.communicationServer + ":" + this.communicationPort;
    }

    public String getBackupServerWithPort() {
        return this.backupServer + ":" + this.backupPort;
    }

    public void init(Map<String, Properties> mp) {
        this.mp = mp;
        parse();
    }

    public void parse() {
        for (Map.Entry<String, Properties> entry : mp.entrySet()) {

            LOG_MSG = "";
            LOG_MSG += "{";
            LOG_MSG += "key: " + entry.getKey();
            LOG_MSG += ", value: " + entry.getValue();
            LOG_MSG += "}";
            log.debug(Utils.prepareLogMessage(LOG_MSG));

            if (entry.getKey().equals(CONFIG_FILE_NAME)) {
                Properties properties = entry.getValue();

                //
                serverPort = properties.getProperty(PROPERTY_NAME_SERVER_PORT);
                LOG_MSG = "";
                LOG_MSG += "serverPort: " + serverPort;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                databaseServer = properties.getProperty(PROPERTY_NAME_DATABASE_SERVER);
                LOG_MSG = "";
                LOG_MSG += "databaseServer: " + databaseServer;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                databasePort = properties.getProperty(PROPERTY_NAME_DATABASE_PORT);
                LOG_MSG = "";
                LOG_MSG += "databasePort: " + databasePort;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                userPassword = EncryptDecryptPwd.decryptKey(properties.getProperty(PROPERTY_NAME_USER_PASSWORD));
                LOG_MSG = "";
                LOG_MSG += "userPassword: " + userPassword;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                billingServer = properties.getProperty(PROPERTY_NAME_BILLING_SERVER);
                LOG_MSG = "";
                LOG_MSG += "billingServer: " + billingServer;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                billingPort = properties.getProperty(PROPERTY_NAME_BILLING_PORT);
                LOG_MSG = "";
                LOG_MSG += "billingPort: " + billingPort;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                billingUsername = properties.getProperty(PROPERTY_NAME_BILLING_USERNAME);
                LOG_MSG = "";
                LOG_MSG += "billingUsername: " + billingUsername;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                billingPassword = EncryptDecryptPwd.decryptKey(properties.getProperty(PROPERTY_NAME_BILLING_PASSWORD));
                LOG_MSG = "";
                LOG_MSG += "billingPassword: " + billingPassword;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                producerServer = properties.getProperty(PROPERTY_NAME_PRODUCER_SERVER);
                LOG_MSG = "";
                LOG_MSG += "producerServer: " + producerServer;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                producerPort = properties.getProperty(PROPERTY_NAME_PRODUCER_PORT);
                LOG_MSG = "";
                LOG_MSG += "producerPort: " + producerPort;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                producerUsername = properties.getProperty(PROPERTY_NAME_PRODUCER_USERNAME);
                LOG_MSG = "";
                LOG_MSG += "producerUsername: " + producerUsername;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                producerPassword = EncryptDecryptPwd.decryptKey(properties.getProperty(PROPERTY_NAME_PRODUCER_PASSWORD));
                LOG_MSG = "";
                LOG_MSG += "producerPassword: " + producerPassword;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                communicationServer = properties.getProperty(PROPERTY_NAME_COMMUNICATION_SERVER);
                LOG_MSG = "";
                LOG_MSG += "communicationServer: " + communicationServer;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                communicationPort = properties.getProperty(PROPERTY_NAME_COMMUNICATION_PORT);
                LOG_MSG = "";
                LOG_MSG += "communicationPort: " + communicationPort;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                communicationUsername = properties.getProperty(PROPERTY_NAME_COMMUNICATION_USERNAME);
                LOG_MSG = "";
                LOG_MSG += "communicationUsername: " + communicationUsername;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                communicationPassword = EncryptDecryptPwd.decryptKey(properties.getProperty(PROPERTY_NAME_COMMUNICATION_PASSWORD));
                LOG_MSG = "";
                LOG_MSG += "communicationPassword: " + communicationPassword;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //backup server
                backupServer = properties.getProperty(PROPERTY_NAME_BACKUP_SERVER);
                LOG_MSG = "";
                LOG_MSG += "backupServer: " + backupServer;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                backupPort = properties.getProperty(PROPERTY_NAME_BACKUP_PORT);
                LOG_MSG = "";
                LOG_MSG += "backupPort: " + backupPort;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                backupUsername = properties.getProperty(PROPERTY_NAME_BACKUP_USERNAME);
                LOG_MSG = "";
                LOG_MSG += "backupUsername: " + backupUsername;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                backupPassword = EncryptDecryptPwd.decryptKey(properties.getProperty(PROPERTY_NAME_BACKUP_PASSWORD));
                LOG_MSG = "";
                LOG_MSG += "backupPassword: " + backupPassword;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                batchThreadNo = properties.getProperty(PROPERTY_NAME_BATCH_THREAD_NO);
                LOG_MSG = "";
                LOG_MSG += "backupPassword: " + backupPassword;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                batchFetchRow = properties.getProperty(PROPERTY_NAME_BATCH_FETCH_ROW);
                LOG_MSG = "";
                LOG_MSG += "backupPassword: " + backupPassword;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                billGeneration = properties.getProperty(PROPERTY_NAME_BILL_GENERATION);
                LOG_MSG = "";
                LOG_MSG += "billGeneration: " + billGeneration;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                prepayStatusCheck = properties.getProperty(PROPERTY_NAME_PREPAY_STATUS_CHECK);
                LOG_MSG = "";
                LOG_MSG += "prepayStatusCheck: " + prepayStatusCheck;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                pdfGeneration = properties.getProperty(PROPERTY_NAME_PDF_GENERATION);
                LOG_MSG = "";
                LOG_MSG += "pdfGeneration: " + pdfGeneration;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                emailSmsNotification = properties.getProperty(PROPERTY_NAME_EMAIL_SMS_NOTIFICATION);
                LOG_MSG = "";
                LOG_MSG += "emailSmsNotification: " + emailSmsNotification;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                dailyTransaction = properties.getProperty(PROPERTY_NAME_DAILY_TRANSACTION);
                LOG_MSG = "";
                LOG_MSG += "dailyTransaction: " + dailyTransaction;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                weeklyTransaction = properties.getProperty(PROPERTY_NAME_WEEKLY_TRANSACTION);
                LOG_MSG = "";
                LOG_MSG += "weeklyTransaction: " + weeklyTransaction;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                monthlyTransaction = properties.getProperty(PROPERTY_NAME_MONTHLY_TRANSACTION);
                LOG_MSG = "";
                LOG_MSG += "monthlyTransaction: " + monthlyTransaction;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                //
                dataArchive = properties.getProperty(PROPERTY_NAME_DATA_ARCHIVE);
                LOG_MSG = "";
                LOG_MSG += "dataArchive: " + dataArchive;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                dummyTask = properties.getProperty(PROPERTY_NAME_DUMMY_TASK);
                LOG_MSG = "";
                LOG_MSG += "dummyTask: " + dummyTask;
                log.debug(Utils.prepareLogMessage(LOG_MSG));

            } else {
                LOG_MSG = "";
                LOG_MSG += "config file name not recognized";
                log.debug(Utils.prepareLogMessage(LOG_MSG));
            }
        }
    }
}