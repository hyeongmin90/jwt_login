package com.example.login.infra;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secretKet}")
    private String secretKey;

    //30분
    public final Long expirationTime = 30 * 60 * 1000L;
    //7일
    public final Long refreshTokenExpirationTime = 7 * 24 * 60 * 60 * 1000L;

    private Key getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public boolean isExpired(String token){
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            log.info("Valid Token: User {}-{}", claims.get("id"), claims.get("name"));
            return false;

        } catch (ExpiredJwtException e) {
            log.info("Token expired: {}", e.getMessage());
        } catch (JwtException e) {
            log.info("Token forgery or other errors: {}", e.getMessage());
        }
        return true;
    }

    public String createAccessToken(Long id, String name) {
        return createToken(id, name, expirationTime);
    }

    public String createRefreshToken(Long id, String name){
        return createToken(id, name, refreshTokenExpirationTime);
    }

    private String createToken(Long id, String name, Long expirationTime) {
        Claims claims = Jwts.claims();
        claims.put("id", id);
        claims.put("name", name);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }


}
