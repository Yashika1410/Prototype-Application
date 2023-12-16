package com.crafters.DataService.utils;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtUtils {
    /**
     * method to extract username from jwt token
     *
     * @param token
     * @return
     */
    String extractUserName(String token);

    /**
     * to generate jwt token
     *
     * @param userDetails
     * @return
     */
    String generateToken(UserDetails userDetails);

    /**
     * to check if token is valid or not
     *
     * @param token
     * @param userDetails
     * @return
     */
    boolean isTokenValid(String token, UserDetails userDetails);

}
