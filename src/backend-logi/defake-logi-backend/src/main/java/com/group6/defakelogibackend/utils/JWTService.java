package com.group6.defakelogibackend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTService {

    private final static SecretKey secretKey;

    static {
        secretKey = Jwts.SIG.HS256.key().build();
    }

    // 生成某一个用户的 JWT 令牌，用于其后续登陆
    public String generateJWT(long id) {
        String jwtToken = Jwts.builder()
                .signWith(secretKey)
                .claim("id", id)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 3)) // 默认过期时间3d
                .compact();
        return jwtToken;
    }

    public String generateJWT(long id, Date expiration) {
        String jwtToken = Jwts.builder()
                .signWith(secretKey)
                .claim("id", id)
                .expiration(expiration)
                .compact();
        return jwtToken;
    }

    // 验证一个令牌是否有效, 并且返回其对应的用户 id
    public String getUserId(String jwtToken) {
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwtToken);
        return claims.getPayload().get("id").toString();
    }

}

