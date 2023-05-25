package com.samnang.project.template.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret:j7e6zqneugzr1h53iut1p1k808rj4qcrx7f74jcuhn000rv857}")
    public String secret;

    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(this.secret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | io.jsonwebtoken.UnsupportedJwtException | io.jsonwebtoken.MalformedJwtException | IllegalArgumentException e) {
            LOGGER.error("token parsing error", e);
            return null;
        }
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    protected Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    protected Date getExpirationTime(int expirationTime) {
        return new Date(System.currentTimeMillis() + expirationTime * 1000L);
    }

    public String generateToken(Map<String, Object> claims, int expirationTime) {
        Date createdTime = new Date();
        Date expirationDate = getExpirationTime(expirationTime);
        byte[] keyBytes = this.secret.getBytes();
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(createdTime)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}
