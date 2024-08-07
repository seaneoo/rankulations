package com.seaneoo.rankulations.security;

import com.seaneoo.rankulations.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${security.jwt.expiration-time}")
    private Long jwtExpiration;

    private SecretKey getSecretSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        var claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(User user) {
        var currentMs = System.currentTimeMillis();
        var issuedAt = new Date(currentMs);
        var expiresAt = new Date(currentMs + jwtExpiration);
        return Jwts.builder()
                .subject(user.getId()
                        .toString())
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .signWith(getSecretSigningKey())
                .compact();
    }

    public boolean isExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    public boolean isValid(String token, User user) {
        var subject = extractSubject(token);
        return subject.equals(user.getId()
                .toString()) && !isExpired(token);
    }
}
