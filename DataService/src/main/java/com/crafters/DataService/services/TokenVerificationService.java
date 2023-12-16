package com.crafters.DataService.services;

import com.crafters.DataService.dtos.SignUpRequestDTO;
import com.crafters.DataService.dtos.SignUpResponseDTO;
import com.crafters.DataService.dtos.VerifyRequestDTO;
import com.crafters.DataService.dtos.VerifyResponseDTO;

/**
 * Service interface for user token verification and user account creation.
 * Defines methods for verifying user tokens and creating new user accounts.
 */
public interface TokenVerificationService {
    /**
     * Verifies a user based on the provided verification request data.
     *
     * @param verifyRequestDTO The data transfer object containing information needed for user verification.
     * @return VerifyResponseDTO containing user details and an access token upon successful verification.
     */
    VerifyResponseDTO verify(VerifyRequestDTO verifyRequestDTO);
    /**
     * Creates a new user account based on the provided sign-up request data.
     *
     * @param signUpRequestDTO The data transfer object containing user registration information.
     * @return SignUpResponseDTO containing the newly created user's details.
     */
    SignUpResponseDTO createUser(SignUpRequestDTO signUpRequestDTO);
}
