package com.example.login.infra;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.util.Collection;
import java.util.Collections;


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

    public Authentication getAuthentication(String token){
        Claims claims = parseClaim(token);

        Long userId = claims.get("id", Long.class);
        String role = claims.get("role", String.class);
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);

        UserDetails principal = new User(userId.toString(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private Claims parseClaim(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (Exception e){
            log.info("Access Token 파싱 실패: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean validateToken(String token){
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

    public String createAccessToken(Long id, String role) {
        return createToken(id, role, expirationTime);
    }

    public String createRefreshToken(Long id, String role){
        return createToken(id, role, refreshTokenExpirationTime);
    }

    private String createToken(Long id, String role, Long expirationTime) {
        Claims claims = Jwts.claims();
        claims.put("id", id);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }


}
