package com.project.rideEase.security;

import com.project.rideEase.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@Slf4j
public class JWTService {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    public String generateAccessToken(User user){

        try {
            return Jwts.builder()
                    .subject(user.getId().toString())
                    .claim("email", user.getEmail())
                    .claim("roles", user.getRoles().toString())
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                    .signWith(getSecretKey())
                    .compact();
        } catch (Exception ex) {
            log.error("Exception occurred in generateAccessToken , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in generateAccessToken: "+ex.getMessage());
        }

    }

    public String generateRefreshToken(User user){

        try {
            return Jwts.builder()
                    .subject(user.getId().toString())
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                    .signWith(getSecretKey())
                    .compact();
        } catch (Exception ex) {
            log.error("Exception occurred in generateRefreshToken , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in generateRefreshToken: "+ex.getMessage());
        }

    }

    public Long generateUserIdFromToken(String token){

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Long.valueOf(claims.getSubject());
        } catch (Exception ex) {
            log.error("Exception occurred in generateUserIdFromToken , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in generateUserIdFromToken: "+ex.getMessage());
        }

    }


    public SecretKey getSecretKey(){

        try {
            return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            log.error("Exception occurred in getSecretKey , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in getSecretKey: "+ex.getMessage());
        }
    }
}
