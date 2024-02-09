package kz.baltabayev.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

public class JwtTokenUtils {

    private String secret;
    private Duration jwtLifetime;

    public JwtTokenUtils(String secret, Duration jwtLifetime) {
        this.secret = secret;
        this.jwtLifetime = jwtLifetime;
    }

    public String generateToken(String login) {
        Claims claims = (Claims) Jwts.claims().subject(login);
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());

        return Jwts.builder()
                .claims(claims)
                .subject(login)
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(signKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey signKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
