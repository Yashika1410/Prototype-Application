package com.crafters.DataService.controllers;

import com.crafters.DataService.dtos.SignUpRequestDTO;
import com.crafters.DataService.dtos.SignUpResponseDTO;
import com.crafters.DataService.dtos.VerifyRequestDTO;
import com.crafters.DataService.dtos.VerifyResponseDTO;
import com.crafters.DataService.services.Impl.TokenVerificationServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling user verification and user creation requests.
 * Provides endpoints for verifying user tokens and creating new user accounts.
 *
 * TODO: Add logging and comments for better code understanding.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class VerifyController {

    private TokenVerificationServiceImpl tokenVerificationService;

    /**
     * Endpoint for verifying user tokens.
     *
     * @param verifyRequestDTO The data transfer object containing information needed for user verification.
     * @return ResponseEntity containing the response data after verifying the user.
     */
    @PostMapping("/verify")
    public ResponseEntity<VerifyResponseDTO> verifyUser(@RequestBody VerifyRequestDTO verifyRequestDTO) {
        return new ResponseEntity<>(tokenVerificationService.verify(verifyRequestDTO), HttpStatus.OK);
    }
    /**
     * Endpoint for creating new user accounts.
     *
     * @param signUpRequestDTO The data transfer object containing user registration information.
     * @return ResponseEntity containing the response data after creating the user account.
     */
    @PostMapping("/user")
    public ResponseEntity<SignUpResponseDTO> createUser(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        return new ResponseEntity<>(tokenVerificationService.createUser(signUpRequestDTO), HttpStatus.CREATED);
    }
}
