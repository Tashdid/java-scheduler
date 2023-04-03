package com.tigerit.smartbill.scheduler.config.openapi;

import com.tigerit.smartbill.common.values.ProjectWideConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

//@Profile("!dev")
//@Profile({"!prod && swagger"})
@Configuration
//https://stackoverflow.com/questions/59357205/springdoc-openapi-apply-default-global-securityscheme-possible
public class OpenApiCustomConfig {

    private static final String SECURITY_SCHEME_NAME = "bearer-jwt";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                //.bearerFormat("JWT")
                                                .bearerFormat("JWT")
                                                .in(SecurityScheme.In.HEADER)
                                                .name("Authorization"))
                )
                .info(new Info()
                        .title(ProjectWideConstants.OpenApiMeta.title)
                        .description(ProjectWideConstants.OpenApiMeta.description)
                        .version(ProjectWideConstants.OpenApiMeta.version)
                )
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(SECURITY_SCHEME_NAME, Arrays.asList("read", "write"))
                );
    }
}