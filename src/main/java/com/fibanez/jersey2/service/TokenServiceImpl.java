package com.fibanez.jersey2.service;

import com.google.inject.Inject;
import io.jsonwebtoken.*;

import javax.inject.Named;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class TokenServiceImpl implements TokenService {

    private static final String JWT_SECRET_PROPERTY = "JWT_SECRET";
    private static final JwtParser jwtParser = Jwts.parser();

    private final String secret;

    @Inject
    public TokenServiceImpl(@Named(JWT_SECRET_PROPERTY) String secret) {
        this.secret = secret;
    }

    public String createJsonWebToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("timestamp", LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String validateToken(String token) {
        Jws<Claims> jwt = jwtParser.setSigningKey(secret).parseClaimsJws(token);
        Long timestamp =  (Long) jwt.getBody().get("timestamp");
        if (isTokenExpired(timestamp)) {
            throw new SecurityException("EXPIRED TOKEN");
        }
        return jwt.getBody().getSubject();
    }

    private boolean isTokenExpired(Long timestamp) {
        LocalDateTime nowLDT = LocalDateTime.now(ZoneId.of("UTC"));
        LocalDateTime minAgoLDT = LocalDateTime.now(ZoneId.of("UTC")).minusMinutes(5);
        LocalDateTime tokenLDT = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("UTC"));
        return tokenLDT.isAfter(nowLDT) || tokenLDT.isBefore(minAgoLDT);
    }

}
