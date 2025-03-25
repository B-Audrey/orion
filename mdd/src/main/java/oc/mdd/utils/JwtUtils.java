package oc.mdd.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${app.secret-key}")
    private String secretKey;

    @Value("${app.expiration-time}")
    private String expirationTime;

    @Value("${app.refresh-expiration-time}")
    private String refreshExpirationTime;

    /**
     * Ask for a token for a user
     *
     * @param userEmail the user email that will be encoded in the token
     * @return the generated token
     */
    public String generateToken(String userEmail) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userEmail);
    }

    /**
     * Ask for a refresh token for a user
     *
     * @param userEmail the user email that will be encoded in the token
     * @return the generated refresh token
     */
    public String generateRefreshToken(String userEmail) {
        Map<String, Object> claims = new HashMap<>();
        return createRefreshToken(claims, userEmail);
    }

    /**
     * Generate an access token with the passed params
     *
     * @param email  the user email that will be encoded in the token
     * @param claims the claims that will be encoded in the token
     * @return the generated token
     */
    private String createToken(Map<String, Object> claims, String email) {
        try {
            int exp = Integer.parseInt(expirationTime);
            return Jwts.builder()
                    .setClaims(claims != null ? claims : new HashMap<>()) // Évitez null
                    .setSubject(email)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + exp))
                    .signWith(SignatureAlgorithm.HS256, getSignKey())
                    .compact();
        } catch (Exception e) {
            log.error("Error while creating token: ", e);
            throw e;
        }
    }

    /**
     * Generate a refresh token with the passed params
     *
     * @param claims the claims that will be encoded in the token
     * @param email  the user email that will be encoded in the token
     * @return the generated refresh token
     */
    private String createRefreshToken(Map<String, Object> claims, String email) {
        try {
            int refreshExp = Integer.parseInt(refreshExpirationTime);
            return Jwts.builder()
                    .setClaims(claims != null ? claims : new HashMap<>()) // Évitez null
                    .setSubject(email)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + refreshExp))
                    .signWith(SignatureAlgorithm.HS256, getSignKey())
                    .compact();
        } catch (Exception e) {
            log.error("Error while creating refresh token: ", e);
            throw e;
        }
    }

    /**
     * Get the key to sign the token
     *
     * @return the key to sign the token
     */
    private Key getSignKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes); // Gère automatiquement la compatibilité avec HS256
    }

    /**
     * Validate the token by comparing the user email in the token with the user details
     * and checking if the token is expired
     *
     * @param token       the token to validate
     * @param userDetails the user details to validate
     * @return true if the token is valid, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        String email = extractUserEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Check if the token is expired
     *
     * @param token the token to check
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        Date expiresAt = extractClaim(token, Claims::getExpiration);
        return expiresAt.before(new Date());
    }

    /**
     * Extract the user email from the token
     *
     * @param token the token to extract the user email from
     * @return the user email
     */
    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract the claims from the token
     *
     * @param token          the token to extract the claims from
     * @param claimsResolver the function to resolve the claims
     * @param <T>            the type of the claims
     * @return the claims
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all the claims from the token
     *
     * @param token the token to extract the claims from
     * @return all the claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody();
    }
}
