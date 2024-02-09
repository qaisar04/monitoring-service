package kz.baltabayev.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kz.baltabayev.service.UserService;

import javax.crypto.SecretKey;
import java.nio.file.AccessDeniedException;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

public class JwtTokenUtils {

    private final String secret;
    private final Duration jwtLifetime;
    private final Key key;
    private final UserService userService;

    public JwtTokenUtils(String secret, Duration jwtLifetime, UserService userService) {
        this.secret = secret;
        this.jwtLifetime = jwtLifetime;
        this.userService = userService;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
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

    public Authentication authentication(String token) throws AccessDeniedException {
        if (!validateToken(token)) {
            throw new AccessDeniedException("Access denied!");
        }
        String login = extractUsername(token);
        userService.getUserByLogin(login);
        return new Authentication(login, true, null);
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

    //todo refactoring
    public boolean validateToken(String token) throws RuntimeException {
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return !claims.getBody().getExpiration().before(new Date());
    }
}
