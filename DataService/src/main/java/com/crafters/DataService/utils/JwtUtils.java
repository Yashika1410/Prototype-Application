package com.crafters.DataService.utils;

import org.springframework.security.core.userdetails.UserDetails;
/**
 * Interface for JWT utility operations, including extracting, generating, and validating JWT tokens.
 */
public interface JwtUtils {
    /**
     * Extracts the username from a JWT token.
     *
     * @param token The JWT token from which to extract the username.
     * @return The username extracted from the token.
     */
    String extractUserName(String token);

    /**
     * Generates a JWT token using user details.
     *
     * @param userDetails The UserDetails object for which to generate the token.
     * @return The generated JWT token.
     */
    String generateToken(UserDetails userDetails);

    /**
     * Checks if a JWT token is valid.
     *
     * @param token       The JWT token to validate.
     * @param userDetails The UserDetails object against which to validate the token.
     * @return True if the token is valid, false otherwise.
     */
    boolean isTokenValid(String token, UserDetails userDetails);

}
