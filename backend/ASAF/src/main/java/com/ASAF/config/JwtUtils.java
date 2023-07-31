package com.ASAF.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtUtils {

    private static final String SECRET_KEY = "yourSecretKey"; // choose a strong secret key
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days in milliseconds

    private static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public static String generateToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, secretKey) // secretKey 사용
                .compact();
    }
}
