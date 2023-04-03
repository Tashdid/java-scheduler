package com.tigerit.smartbill.scheduler.config.props;

import com.tigerit.smartbill.common.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import static com.tigerit.smartbill.common.util.Constants.LOG_MSG;

@Slf4j
@DependsOn(
        //"PropertiesConfig"
        //"CustomAppProperties"
        {
                "customAppProperties"
        }
)
@Configuration
public class CustomContainer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    @Autowired
    CustomAppProperties customAppProperties;

    public void customize(ConfigurableServletWebServerFactory factory) {
        //factory.setPort(8042);

        //LOG_MSG = "";
        //LOG_MSG += billingConfigYml.getValx();
        //LOG_MSG += ", ";
        //LOG_MSG += billingConfigYml2.getValA();
        //log.debug(Utils.prepareLogMessage(LOG_MSG));

        String serverPort = "";
        serverPort = customAppProperties.getServerPort();

        Integer serverPortInt = Integer.valueOf(serverPort);

        factory.setPort(serverPortInt);

        LOG_MSG = "";
        LOG_MSG += "serverPortInt: " + serverPortInt;
        log.debug(Utils.prepareLogMessage(LOG_MSG));

    }
}