package com.websocket.websocketredis.chat.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider implements InitializingBean {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private Key keyAccess;

    private long tokenValidMilisecond = 1000L * 60 * 60; // 1시간만 토큰 유효

    @Override
    public void afterPropertiesSet() {
        byte[] keyAccessByte = Decoders.BASE64.decode(secretKey);
        this.keyAccess = Keys.hmacShaKeyFor(keyAccessByte);
    }

    /**
     * 이름으로 Jwt Token을 생성한다.
     */
    public String generateToken(String name) {
        Date now = new Date();

        return Jwts.builder()
            .setId(name)
            .setIssuedAt(now) // 토큰 발행일자
            .setExpiration(new Date(now.getTime() + tokenValidMilisecond)) // 유효시간 설정
            .signWith(keyAccess, SignatureAlgorithm.HS512) // 암호화 알고리즘, secret값 세팅
            .compact();
    }

    /**
     * Jwt Token을 복호화 하여 이름을 얻는다.
     */
    public String getUserNameFromJwt(String jwt) {
        return getClaims(jwt).getBody().getId();
    }

    /**
     * Jwt Token의 유효성을 체크한다.
     */
    public boolean validateToken(String jwt) {
        return this.getClaims(jwt) != null;
    }

    private Jws<Claims> getClaims(String jwt) {
        try {
            return Jwts.parserBuilder().setSigningKey(keyAccess).build().parseClaimsJws(jwt);
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            throw ex;
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
            throw ex;
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            throw ex;
        }
    }
}