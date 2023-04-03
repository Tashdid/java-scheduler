package com.tigerit.smartbill.scheduler.config.security;

import com.tigerit.smartbill.common.property.exposer.ICustomPropertyResolutionService;
import com.tigerit.smartbill.scheduler.config.security.um.JwtAuthenticationEntryPoint;
import com.tigerit.smartbill.scheduler.config.security.um.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Lazy
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",

                //Swagger-UI
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    //@Lazy
    @Primary
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //region [CORS]
    ICustomPropertyResolutionService customPropertyResolutionService; //can be autowired since its package is mentioned in the scanBasePackage of main entry class of this module

    @Autowired
    public void setCustomPropertyResolutionService(ICustomPropertyResolutionService customPropertyResolutionService) {
        this.customPropertyResolutionService = customPropertyResolutionService;
    }

    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    public void setUnauthorizedHandler(JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
    }

    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public void setJwtAuthenticationFilter(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /*//#region [ssl] //note: currently commented out since both http(10081) and https(8081) are being allowed for now
                .requiresChannel()
                    .anyRequest()
                    .requiresSecure()
                    .and()
                //#endregion [ssl]*/
                .cors()
                    .and()
                //.cors(withDefaults()) //not needed, but later may enable this if CORS gives issues
                    //.and()
                .csrf()
                    .disable()
                .exceptionHandling()
                    //.authenticationEntryPoint(customBasicAuthenticationEntryPoint) //todo: comment out or keep?
                    .authenticationEntryPoint(unauthorizedHandler)
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .antMatchers(
                            //"/",
                            "/favicon.ico",
                            "/**/*.png",
                            "/**/*.gif",
                            "/**/*.svg",
                            "/**/*.jpg",
                            "/**/*.html",
                            "/**/*.css",
                            "/**/*.js")
                        .permitAll()
                    //.antMatchers("/api/auth/**")
                        //.permitAll()
                    //region [Comment out some lines]
                    //.antMatchers(HttpMethod.OPTIONS, "/**")
                    //    .permitAll() //for CORS preflight //check: if need to be uncommented
                    //.antMatchers(HttpMethod.OPTIONS, "/")
                    //    .permitAll() //for CORS preflight //check: if need to be uncommented
                    //.antMatchers("/")
                        //.permitAll() //check: if need to be uncommented
                    //endregion [Comment out some lines]
                    //.antMatchers(HttpMethod.GET, "/scheduler/**")
                    //    .permitAll() //todo: try to comment this out
//                    .antMatchers(HttpMethod.GET, "/scheduler/swagger-ui/**")
//                        .permitAll()
                    .antMatchers(HttpMethod.GET, "/scheduler/api-docs/**")
                        .permitAll()
                    .antMatchers(HttpMethod.GET, "/scheduler/swagger-ui.html")
                        .permitAll()
//                    .antMatchers(HttpMethod.GET, "/scheduler/swagger-ui.html/index.html")
//                        .permitAll()
//                    .antMatchers(HttpMethod.GET, "/scheduler/swagger-ui.html/**")
//                        .permitAll()
                    .antMatchers(HttpMethod.GET, "/scheduler/swagger-ui/index.html?**")
                        .permitAll()
//                    //[Swagger]
                    .antMatchers(
//                            "/swagger-ui/index.html",
//                            "/swagger-ui/swagger-ui-bundle.js",
//                            "/swagger-ui/swagger-ui-standalone-preset.js",
                            "/api-docs/swagger-config",
//                            "/swagger-ui/favicon-32x32.png",
//                            "/api-docs/swagger-config",
                            "/api-docs"//,
//                            "/swagger-ui/swagger-ui.css"
                    )
                        .permitAll()
                    //.antMatchers(HttpMethod.GET, "/api/v1/config/status")
                    //    .permitAll()

                    //TODO: NEED TO REMOVE HARD CODED TEXTs as much as possible from here

                    //[Allowing Business Customer creation] [for now] [later, only business_admin will have access]
                    //[note:] Legacy endpoints.

                    //Now, securing all other endpoints
                    .anyRequest()
                        .authenticated() //check: may uncomment. May temporary comment out due to frontend testing server CORS!
                    ;
        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
    //endregion [Sign UP + Sign IN]
}