package com.tigerit.smartbill.scheduler.config.props;

import com.tigerit.smartbill.common.util.Utils;
import com.tigerit.smartbill.scheduler.service.network.TigerITBackupServer;
import com.tigerit.smartbill.scheduler.service.network.TigerITBillingServer;
import com.tigerit.smartbill.scheduler.service.network.TigerITCommunicationServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import static com.tigerit.smartbill.common.values.ProjectWideConstants.ServiceDiscovery.LOAD_BALANCER_BEAN;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Configuration
public class PropertiesConfig {

    private final static String[] PROPERTIES_FILENAMES = {
            "scheduler_config.yml"
    };
    public static String currentCatalinaBaseDir = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    Map<String, Properties> mp;

    @Value("${custom_properties.location}")
    private String custom_propertiesLocation;

    @Bean(
            //name = "customAppProperties"
    )
    CustomAppProperties customAppProperties() {
        mp = stream(PROPERTIES_FILENAMES)
                .collect(toMap(filename -> filename, this::loadProperties));

        CustomAppProperties customAppProperties = new CustomAppProperties();
        customAppProperties.init(mp);

        TigerITBillingServer.configure(customAppProperties);

        TigerITBackupServer.configure(customAppProperties);
        TigerITCommunicationServer.configure(customAppProperties);

        return customAppProperties;
    }


    //@DependsOn(
    //        {
    //                "propertySourcesPlaceholderConfigurer"
    //        }
    //)
    //@Bean(
    //
    //)
    //BillingConfigYml billingConfigYml() {
    //    BillingConfigYml billingConfigYml = new BillingConfigYml();
    //    return billingConfigYml;
    //}

//    @Bean(
//            name = "propertySourcesPlaceholderConfigurer"
//    )
//    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
//        PropertySourcesPlaceholderConfigurer pspc
//                = new PropertySourcesPlaceholderConfigurer();
//        
//        final Resource[] possiblePropertiesResources = {
//                new PathResource("config/props/" + "billing_config.yml")
//        };
//        final Resource resource = stream(possiblePropertiesResources)
//                .filter(Resource::exists)
//                .reduce((previous, current) -> current)
//                .get();
//
//        //Resource[] resources = new ClassPathResource[ ]
//        //        {
//        //                //new ClassPathResource( "foo.properties" )
//        //                new PathResource("config/props/" + "billing_config.yml")
//        //        };
//
//        pspc.setLocation(resource);
//        pspc.setIgnoreUnresolvablePlaceholders( true );
//        return pspc;
//    }

    private Properties loadProperties(final String filename) {
        //String currentCatalinaBaseDir = System.getProperty("catalina.base"); //"user.dir");
        String billingConfigUri = currentCatalinaBaseDir + custom_propertiesLocation + filename;
        if (billingConfigUri.charAt(0) == '/') {
            billingConfigUri = billingConfigUri.substring(1);
        }
        //String billingConfigUri = custom_propertiesLocation + filename;
        final Resource[] possiblePropertiesResources = {
                //new ClassPathResource(filename),
                //new PathResource("config/props/" + filename) //getting right file path
                new PathResource(billingConfigUri) //getting right file path
                //, new PathResource(getCustomPath(filename))
                //, new FileSystemResource(getCustomPath(filename))
        };
        final Resource resource = stream(possiblePropertiesResources)
                .filter(Resource::exists)
                .reduce((previous, current) -> current)
                .get();
        final Properties properties = new Properties();

        try {
            properties.load(resource.getInputStream());
        } catch (final IOException exception) {
            log.error(exception.getMessage());
            throw new RuntimeException(exception);
        }

        log.info(Utils.prepareLogMessage("Using {} as user resource"), resource); //check

        return properties;
    }

    @LoadBalanced
    @Bean
    @Qualifier(LOAD_BALANCER_BEAN)
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /*private String getCustomPath(final String filename) {
        String customPath =
                (custom_propertiesLocation.endsWith(".properties") || custom_propertiesLocation.endsWith(".yml"))
        ? custom_propertiesLocation : custom_propertiesLocation + filename;

        return customPath;
    }*/
}
