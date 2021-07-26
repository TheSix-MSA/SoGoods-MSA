package org.thesix.gatewayserver.config;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@Log4j2
public class JwtValidator {

    @Value("${org.secret.key}")
    private String jwtSecret;

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes("UTF-8"))
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Signature error");
        } catch (MalformedJwtException ex) {
            log.error("Malformed token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        } catch (NoClassDefFoundError ex) {
            log.error("Not Valid Token");
        } catch (UnsupportedEncodingException e) {
            log.error("Not Valid Token");
        }
        return false;
    }

    public Claims extractAllClaims(String token) throws ExpiredJwtException {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(jwtSecret.getBytes("UTF-8"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
