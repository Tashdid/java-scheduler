//package com.tigerit.smartbill.scheduler.launcher;
//
//import com.tigerit.smartbill.scheduler.service.BillingServerRemoteAPI;
////import com.tigerit.smartbill.scheduler.service.monitoring.SchedulerConfiguration;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.config.ConfigurableBeanFactory;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.annotation.Scope;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
////import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ScheduledFuture;
//
//import javax.annotation.PreDestroy;
//
//@Component
//@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
//public class SchedulerAppListener {
//
//    //public static Map<String, ScheduledFuture<?>> hashMap = null;//new HashMap<>();
//    private BillingServerRemoteAPI billingServerRemoteAPI;
//
//    @Autowired
//    public void setSchedulerRemoteAPI(BillingServerRemoteAPI billingServerRemoteAPI) {
//        this.billingServerRemoteAPI = billingServerRemoteAPI;
//    }
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void doSomethingAfterStartup() {
//        //hashMap = billingServerRemoteAPI.GetSchedulerList();
//    	Map<String, ScheduledFuture<?>> hashMap = billingServerRemoteAPI.registerServer();
//    	//SchedulerConfiguration.configure(hashMap);
//    }
//    
//    @PreDestroy
//    public void onDestroy() throws Exception {
//        //System.out.println("Spring Container is destroyed!");
//    	billingServerRemoteAPI.unregisterServer();
//    }
//}
