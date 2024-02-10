package kz.baltabayev.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kz.baltabayev.service.UserService;

import javax.crypto.SecretKey;
import java.nio.file.AccessDeniedException;
import java.time.Duration;
import java.util.Date;

public class JwtTokenUtils {

    private final String secret;
    private final Duration jwtLifetime;
    private final UserService userService;

    public JwtTokenUtils(String secret, Duration jwtLifetime, UserService userService) {
        this.secret = secret;
        this.jwtLifetime = jwtLifetime;
        this.userService = userService;
    }

    public String generateToken(String login) {
        ClaimsBuilder claimsBuilder = Jwts.claims().subject(login);
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());

        return Jwts.builder()
                .claims(claimsBuilder.build())
                .subject(login)
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(signKey())
                .compact();
    }

    public Authentication authentication(String token) throws AccessDeniedException {
        if (!validateToken(token)) {
            throw new AccessDeniedException("Access denied!");
        }

        String login = extractLogin(token);
        userService.getUserByLogin(login);
        return new Authentication(login, true, null);
    }

    public String extractLogin(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) throws RuntimeException {
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(signKey())
                .build()
                .parseSignedClaims(token);

        return !claims.getPayload().getExpiration().before(new Date());
    }

    private SecretKey signKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
