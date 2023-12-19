package com.crafters.DataService.services;

import com.crafters.DataService.dtos.SignUpRequestDTO;
import com.crafters.DataService.dtos.LoginRequestDTO;

import org.springframework.security.core.Authentication;

import com.crafters.DataService.dtos.AuthResponseDTO;

/**
 * Service interface for user token verification and user account creation.
 * Defines methods for verifying user tokens and creating new user accounts.
 */
public interface AuthService {
    /**
     * Verifies a user based on the provided verification request data.
     *
     * @return AuthResponseDTO containing user details and an access token upon successful verification.
     */
    AuthResponseDTO verify(LoginRequestDTO loginRequestDTO);
    /**
     * Creates a new user account based on the provided sign-up request data.
     *
     * @return AuthResponseDTO containing the newly created user's details.
     */
    AuthResponseDTO createUser(SignUpRequestDTO signUpRequestDTO);

    /**
     * Return user id of the authenticated user.
     *
     * @return String containing the unique user id.
     */
    String getUserId(Authentication authentication);
}
