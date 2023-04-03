package com.tigerit.smartbill.scheduler.service.monitoring;

import com.tigerit.smartbill.common.util.DynamicGlobals;
import com.tigerit.smartbill.common.util.Utils;
import com.tigerit.smartbill.scheduler.service.network.TigerITBackupServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.tigerit.smartbill.common.values.APIValues.BackupServer.ArchiveServiceProvider.ArchiveServiceProvider_BASE_PATH;
import static com.tigerit.smartbill.common.values.APIValues.BackupServer.ArchiveServiceProvider.BATCH_ARCHIVE;

@Component
@Slf4j
public class DataArchiveScheduler {

    public void initDataArchiveScheduler() {
        //Setting up today value
        if (DynamicGlobals.useActualTodayValue) {
            String actualTodayValue = ""; //eg: format: 3/5/2019
            Date date = new Date();
            actualTodayValue = Utils.getDayStringFromJavaDate(date);
            DynamicGlobals.todayString = actualTodayValue;
        }

        Object payload = null;

        String fullPOST_Url = ArchiveServiceProvider_BASE_PATH + BATCH_ARCHIVE;

        try {
        	TigerITBackupServer tigerITBackupServer = new TigerITBackupServer();
        	payload = tigerITBackupServer.getForObject(fullPOST_Url);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        if (payload != null) {
        }
    }

}
