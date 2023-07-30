//// JWT 토큰 생성과 검증을 처리하기 위한 JwtTokenProvider 클래스
//package com.ASAF.config;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.servlet.http.HttpServletRequest;
//import java.util.Base64;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class JwtTokenProvider {
//
//    @Value("${jwt.secretKey}")
//    private String secretKey;
//
//    @Value("${jwt.tokenValidityInMilliseconds}")
//    private long validityInMilliseconds;
//
//    @PostConstruct
//    protected void init() {
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
//    }
//
//    public String createToken(Authentication authentication) {
//        String username = authentication.getName();
//        List<String> roles = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//
//        Claims claims = Jwts.claims().setSubject(username);
//        claims.put("roles", roles);
//
//        Date now = new Date();
//        Date validity = new Date(now.getTime() + validityInMilliseconds);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(validity)
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//    }
//
//    public String createTokenFromUserDetails(UserDetails userDetails) {
//        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
//        claims.put("roles", userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList()));
//
//        Date now = new Date();
//        Date validity = new Date(now.getTime() + validityInMilliseconds);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(validity)
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//    }
//
//}
//
