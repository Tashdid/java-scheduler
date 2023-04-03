package com.tigerit.smartbill.scheduler.config.security.um;

import com.tigerit.smartbill.common.model.consts.enums.SystemUserTypeNameENUM;
import com.tigerit.smartbill.common.model.dto.JwtCustomPayload;
import com.tigerit.smartbill.common.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.tigerit.smartbill.common.util.Constants.LOG_MSG;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private CustomJwtTokenManager customJwtTokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //todo: check if can optimize the db calls. Currently, on each request, many db calls(ie: checking user type by querying multiple tables) are made
        String requestUrl = request.getRequestURI();
        String requestMethod = request.getMethod();

        LOG_MSG = "";
        LOG_MSG += "requestUrl : " + requestUrl;
        LOG_MSG += ", ";
        LOG_MSG += "requestMethod : " + requestMethod;
        log.debug(Utils.prepareLogMessage(LOG_MSG));

        //For test
        //note: //todo

        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)
                    && customJwtTokenManager.validateToken(jwt)
                //&& SecurityContextHolder.getContext().getAuthentication() == null
            ) {
                //Long userId = customJwtTokenManager.getUserIdFromJWT(jwt);
                JwtCustomPayload jwtCustomPayload = customJwtTokenManager.get_CustomJwtPayload_FromJWT(jwt);

                if (jwtCustomPayload == null) {
                    LOG_MSG = "";
                    LOG_MSG += "jwtCustomPayload == null.";
                    LOG_MSG += " Aborting.";
                    log.error(Utils.prepareLogMessage(LOG_MSG));

                    return;
                }

                LOG_MSG = "";
                LOG_MSG += "jwtCustomPayload : " + jwtCustomPayload.toString();
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                UserDetails userDetails = UserPrincipal.create(jwtCustomPayload);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                //note: no db access control code here. Instead, the extracted user_type can be put to use
                //SystemUserTypeNameENUM systemUserTypeNameENUM = SystemUserTypeNameENUM.UNKNOWN_SYSTEM_USER_TYPE;
                String systemUserTypeName_String = SystemUserTypeNameENUM.UNKNOWN_SYSTEM_USER_TYPE.getValue();

                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                String userTypeName = Objects.requireNonNull(SystemUserTypeNameENUM.byOrdinal(userPrincipal.getSystemUserTypeId())).getValue();

                if (
                        org.apache.commons.lang3.StringUtils.isNotBlank(
                                userTypeName //jwtCustomPayload.getUserTypeName()
                        )
                ) {
                    systemUserTypeName_String = userTypeName; //jwtCustomPayload.getUserTypeName();
                } else {
                    LOG_MSG = "";
                    LOG_MSG += "jwtCustomPayload.getUserTypeName() is not ok";
                    log.error(Utils.prepareLogMessage(LOG_MSG));
                }

                LOG_MSG = "";
                LOG_MSG += "API was called by 'system_user_type' : '" + systemUserTypeName_String + "'";
                log.debug(Utils.prepareLogMessage(LOG_MSG));

                LOG_MSG = "JWT token read from http REQUEST";
                LOG_MSG += "\n username: " + userDetails.getUsername();
                //LOG_MSG += "\n password: " + userDetails.getPassword();
                LOG_MSG += " \n jwt: " + jwt;
                LOG_MSG = Utils.prepareLogMessage(LOG_MSG);
                log.info(LOG_MSG);
            }
        } catch (Exception ex) {
            LOG_MSG = "";
            LOG_MSG += "An exception occurred while extracting information from the jwt token. See exception details.";
            LOG_MSG += ", ";
            LOG_MSG += "requestUrl : " + requestUrl;
            LOG_MSG += ", ";
            LOG_MSG += "requestMethod : " + requestMethod;
            log.error(Utils.prepareLogMessage(LOG_MSG), ex);
        }
        //check:  Expired JWT token!!!

        //todo:
        //issue: expired jwt token is getting accepted here!
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else {
            LOG_MSG = " jwt token not found in request";
            LOG_MSG = Utils.prepareLogMessage(LOG_MSG);
            log.warn(LOG_MSG);
        }
        return null;
    }
}