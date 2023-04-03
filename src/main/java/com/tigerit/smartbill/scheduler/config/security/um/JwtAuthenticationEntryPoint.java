package com.tigerit.smartbill.scheduler.config.security.um;

import com.tigerit.smartbill.common.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.tigerit.smartbill.common.util.Constants.LOG_MSG;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        //String requestURI = httpServletRequest.getRequestURI();
        //String requestMethod = httpServletRequest.getMethod();
        LOG_MSG = "Responding with unauthorized error. Message - {}";
        LOG_MSG = Utils.prepareLogMessage(LOG_MSG);
        log.error(LOG_MSG, e.getMessage());
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    }
}