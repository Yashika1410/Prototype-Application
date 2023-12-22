package com.crafters.DataService.utils.Impl;

import com.crafters.DataService.exceptions.JWTExpirationException;
import com.crafters.DataService.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.crafters.DataService.constants.JwtConstants.SECRET_KEY;

/**
 * Implementation of the JwtUtils interface, providing functionality for JWT token operations.
 */
@Component
public class JwtUtilsImpl implements JwtUtils {
    /**
     * Extracts the username from a JWT token.
     *
     * @param token The JWT token from which to extract the username.
     * @return The username extracted from the token.
     */
    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Generates a JWT token using user details.
     *
     * @param userDetails The UserDetails object for which to generate the token.
     * @return The generated JWT token.
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails);
    }

    /**
     * Checks if a JWT token is valid.
     *
     * @param token       The JWT token to validate.
     * @param userDetails The UserDetails object against which to validate the token.
     * @return True if the token is valid, false otherwise.
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    //TODO: convert expiryTime to constant
    private String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24*24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    private boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        }catch (JwtException ex) {
            throw new JWTExpirationException("Jwt token is expired");
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
