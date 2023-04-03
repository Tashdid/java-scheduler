package com.tigerit.smartbill.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


//@EnableEurekaClient
//@RibbonClient(name = "ping-a-server", configuration = RibbonConfiguration.class)
//@EnableDiscoveryClient
//Following three lines very crucial since this app main class is in a sub package
@SpringBootApplication(
        scanBasePackages = {
            "com.tigerit.smartbill.common",
            "com.tigerit.smartbill.common.config.log.api.impl1", //[api logging]
            "com.tigerit.smartbill.common.errorhandling.api.errorapp2", //[api error code + request/response validation]
            "com.tigerit.smartbill.scheduler"
        },
        exclude = {
                MongoAutoConfiguration.class,
                MongoDataAutoConfiguration.class,

                //region [db]
                    //[note]
                        //[1] works as expected by excluding db auto config (ie: runtime error goes away even if db related dependencies are there in the common's build.gradle).
                DataSourceAutoConfiguration.class,
//                DataSourceTransactionManagerAutoConfiguration.class,
//                HibernateJpaAutoConfiguration.class,
//                JpaRepositoriesAutoConfiguration.class
                //endregion [db]
        }
)
public class SchedulerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SchedulerApplication.class);
    }

}
