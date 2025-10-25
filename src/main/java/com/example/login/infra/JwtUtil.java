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

    @Value("${jwt.access-secretKey}")
    private String accessSecretKey;
    @Value("${jwt.refresh-secretKey}")
    private String refreshSecretKey;

    //30분
    public final Long expirationTime = 30 * 60 * 1000L;
    //7일
    public final Long refreshTokenExpirationTime = 7 * 24 * 60 * 60 * 1000L;

    private Key getAccessKey() {
        return Keys.hmacShaKeyFor(accessSecretKey.getBytes());
    }

    private Key getRefreshKey() {
        return Keys.hmacShaKeyFor(refreshSecretKey.getBytes());
    }

    public Authentication getAuthentication(String token){
        Claims claims = parseClaim(token, getAccessKey());
        String subject = claims.getSubject();
        String role = claims.get("role", String.class);

        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);

        UserDetails principal = new User(subject, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public UserInfo getInfoFromRefreshToken(String token){
        Claims claims = parseClaim(token, getRefreshKey());
        return UserInfo.builder()
                .id(Long.parseLong(claims.getSubject()))
                .role(claims.get("role", String.class))
                .build();
    }

    private Claims parseClaim(String token, Key key){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (Exception e){
            log.info("Access Token 파싱 실패: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean validateAccessToken(String accessToken){
        return validateToken(accessToken, getAccessKey());
    }

    public boolean validateRefreshToken(String refreshToken){
        return validateToken(refreshToken, getRefreshKey());
    }

    private boolean validateToken(String token, Key key){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다. Token: {}", token);
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다. Token: {}", token);
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰입니다. Token: {}", token);
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 잘못되었습니다. Token: {}", token);
        } catch (JwtException e) {
            log.error("JWT 처리 중 알 수 없는 오류가 발생했습니다. Token: {}", token, e);
        }
        return false;
    }

    public String createAccessToken(Long id, String role) {
        return createToken(id, role, getAccessKey(), expirationTime);
    }

    public String createRefreshToken(Long id, String role){
        return createToken(id, role, getRefreshKey(), refreshTokenExpirationTime);
    }

    private String createToken(Long id, String role, Key key, Long expirationTime) {
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


}
