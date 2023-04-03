package com.tigerit.smartbill.scheduler.config.security.um;

import com.tigerit.smartbill.common.model.dto.JwtCustomPayload;
import com.tigerit.smartbill.common.util.Utils;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.tigerit.smartbill.common.util.Constants.LOG_MSG;

@Slf4j
@Component
public class CustomJwtTokenManager {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private long jwtExpirationInMs;

    //supposed to be not used
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public JwtCustomPayload get_CustomJwtPayload_FromJWT(String token) {
        JwtCustomPayload jwtCustomPayload = null;
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        String jwtCustomPayload_JsonString = claims.getSubject();
        Object object = Utils.jSONStringToObject(jwtCustomPayload_JsonString, JwtCustomPayload.class);
        if (object instanceof JwtCustomPayload) {
            jwtCustomPayload = (JwtCustomPayload) object;
        } else {
            LOG_MSG = "";
            LOG_MSG += "could not convert json to target object";
            log.error(Utils.prepareLogMessage(LOG_MSG));
        }
        return jwtCustomPayload;
    }

    public boolean validateToken(String authToken) {
        LOG_MSG = "";
        try {
            //check: if actually validation is working. aka: Check if it is the best way (ie: by throwing exceptions)
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            LOG_MSG += "Invalid JWT signature";
            log.error(Utils.prepareLogMessage(LOG_MSG), ex);
        } catch (MalformedJwtException ex) {
            LOG_MSG += "Invalid JWT signature malformed";
            log.error(Utils.prepareLogMessage(LOG_MSG), ex);
        } catch (ExpiredJwtException ex) {
            LOG_MSG = "";
            LOG_MSG += "Found an expired JWT token. ";
            LOG_MSG += "(Later, may have to investigate if this is a security issue/concern)";
            LOG_MSG += "For now, proceeding to check if this expiration is within limit as per policy. ";
            log.warn(Utils.prepareLogMessage(LOG_MSG), ex);

            //log.error(Utils.prepareLogMessage(LOG_MSG), ex);

            /*if(isExpirationWithAcceptableTerms(authToken)) {
                LOG_MSG = "";
                LOG_MSG += "JWT token is expired. But within acceptable delay. Proceeding to accept this jwt token";
                LOG_MSG += "returning 'true'";
                log.warn(Utils.prepareLogMessage(LOG_MSG));

                return true;
            } else {
                LOG_MSG = "";
                LOG_MSG += "The expired jwt token is not acceptable as per set policy. ";
                LOG_MSG += "(Later, may have to investigate if this is a security issue/concern). ";
                LOG_MSG += "Considering this jwt token as invalid.";
                log.error(Utils.prepareLogMessage(LOG_MSG), ex);

                return false;
            }*/

        } catch (UnsupportedJwtException ex) {
            LOG_MSG += "Unsupported JWT token";
            log.error(Utils.prepareLogMessage(LOG_MSG), ex);
        } catch (IllegalArgumentException ex) {
            LOG_MSG += "JWT claims string is empty.";
            log.error(Utils.prepareLogMessage(LOG_MSG), ex);
        }
        return false;
    }

    /*private boolean isExpirationWithAcceptableTerms(String authToken) {
        //todo:
            //actually implement
        boolean result = false;
        try {
            //check: if actually validation is working. aka: Check if it is the best way (ie: by throwing exceptions)
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
        } catch (ExpiredJwtException ex) {
            LOG_MSG = "";
            LOG_MSG += "expired jwt";
            log.error(Utils.prepareLogMessage(LOG_MSG), ex);
        }
        return result;
    }*/
}
