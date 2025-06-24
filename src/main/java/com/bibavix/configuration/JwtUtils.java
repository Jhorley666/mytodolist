package com.bibavix.configuration;

import com.bibavix.dto.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtUtils {
    private final AppProperties appProperties;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl)
                authentication.getPrincipal();
        String secret = appProperties.getAuth().getTokenSecret(); // get from config
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + appProperties.getAuth().getTokenExpirationMs()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    public String getUsernameFromToken(String token) {
        String secret = appProperties.getAuth().getTokenSecret();
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            String secret = appProperties.getAuth().getTokenSecret();
            Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("Jwt authentication failed!{0}", ex);
        }
        return false;
    }

}
