package kz.baltabayev.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kz.baltabayev.model.User;
import kz.baltabayev.service.UserService;

import javax.crypto.SecretKey;
import java.nio.file.AccessDeniedException;
import java.time.Duration;
import java.util.Date;

/**
 * Utility class for handling JWT token operations.
 */
public class JwtTokenUtils {

    private final String secret;
    private final Duration jwtLifetime;
    private final UserService userService;

    /**
     * Constructor for JwtTokenUtils.
     *
     * @param secret the secret key for signing JWT
     * @param jwtLifetime the lifetime of the JWT
     * @param userService the user service for user operations
     */
    public JwtTokenUtils(String secret, Duration jwtLifetime, UserService userService) {
        this.secret = secret;
        this.jwtLifetime = jwtLifetime;
        this.userService = userService;
    }

    /**
     * Generates a JWT for the given login.
     *
     * @param login the login for which to generate the JWT
     * @return the generated JWT
     */
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

    /**
     * Authenticates a user based on the given JWT.
     *
     * @param token the JWT to authenticate
     * @return the authentication result
     * @throws AccessDeniedException if the JWT is invalid or the user does not exist
     */
    public Authentication authentication(String token) throws AccessDeniedException {
        if (!validateToken(token)) {
            throw new AccessDeniedException("Access denied: Invalid token");
        }

        String login = extractLogin(token);
        User user = userService.getUserByLogin(login)
                .orElseThrow(() -> new AccessDeniedException("Access denied: User not found"));

        return new Authentication(login, user.getRole(), true, "Successful login");
    }

    /**
     * Extracts the login from the given JWT.
     *
     * @param token the JWT from which to extract the login
     * @return the extracted login
     */
    public String extractLogin(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extracts all claims from the given JWT.
     *
     * @param token the JWT from which to extract the claims
     * @return the extracted claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Validates the given JWT.
     *
     * @param token the JWT to validate
     * @return true if the JWT is valid, false otherwise
     * @throws RuntimeException if the JWT is invalid
     */
    public boolean validateToken(String token) throws RuntimeException {
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(signKey())
                .build()
                .parseSignedClaims(token);

        return !claims.getPayload().getExpiration().before(new Date());
    }

    /**
     * Generates the secret key for signing JWT.
     *
     * @return the generated secret key
     */
    private SecretKey signKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
