package com.example.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for JSON Web Token (JWT) operations.
 * <p>
 * Provides functionality for generating, parsing, and validating JWT tokens
 * used for authentication. Uses HMAC-SHA256 algorithm for signing tokens.
 *
 * <p>Key features:
 * <ul>
 *   <li>Token generation with configurable expiration</li>
 *   <li>Token validation against user details</li>
 *   <li>Claims extraction from tokens</li>
 *   <li>Automatic secret key generation</li>
 * </ul>
 *
 * <p>Tokens are configured with a 24-hour expiration time by default.
 *
 * @see Component
 * @see Jwts
 */
@Component
public class JwtUtils {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    /**
     * Generates a JWT token for the specified user details.
     *
     * @param userDetails the user details to include in the token
     * @return the generated JWT token string
     * @throws IllegalArgumentException if userDetails is null
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Creates a JWT token with the specified claims and subject.
     *
     * @param claims additional claims to include in the token
     * @param subject the principal that is the subject of the token
     * @return the generated JWT token string
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Validates a JWT token against the provided user details.
     *
     * @param token the JWT token to validate
     * @param userDetails the user details to validate against
     * @return true if the token is valid for the given user, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extracts the username (subject) from a JWT token.
     *
     * @param token the JWT token to parse
     * @return the username contained in the token
     * @throws io.jsonwebtoken.JwtException if the token is invalid
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from a JWT token.
     *
     * @param <T> the type of the claim to extract
     * @param token the JWT token to parse
     * @param claimsResolver function to extract the desired claim
     * @return the extracted claim value
     * @throws io.jsonwebtoken.JwtException if the token is invalid
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses and extracts all claims from a JWT token.
     *
     * @param token the JWT token to parse
     * @return the claims contained in the token
     * @throws io.jsonwebtoken.JwtException if the token is invalid
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if a JWT token has expired.
     *
     * @param token the JWT token to check
     * @return true if the token has expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from a JWT token.
     *
     * @param token the JWT token to parse
     * @return the expiration date contained in the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}