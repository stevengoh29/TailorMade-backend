package com.future.tailormade.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private String jwtExpirationTime;

    public String getKey() {
        return Base64.getEncoder().encodeToString(jwtSecret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserIdFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
//
//    public String generateToken(User user) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("ROLE", user.getRoles());
//        return doGenerateToken(claims, user.getId());
//    }
//
//    public String doGenerateToken(String userId) {
//        Date createdDate = new Date();
//        Date expirationDate = new Date(createdDate.getTime() + jwtExpirationTime);
//
//        return Jwts.builder()
//                .setSubject(userId)
//                .setIssuedAt(createdDate)
//                .setExpiration(expirationDate)
//                .signWith(SignatureAlgorithm.RS512, jwtSecret)
//                .compact();
//    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
