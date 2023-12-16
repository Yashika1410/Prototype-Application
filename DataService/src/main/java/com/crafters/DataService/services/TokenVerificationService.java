package com.crafters.DataService.services;

import com.crafters.DataService.dtos.SignUpRequestDTO;
import com.crafters.DataService.dtos.SignUpResponseDTO;
import com.crafters.DataService.dtos.VerifyRequestDTO;
import com.crafters.DataService.dtos.VerifyResponseDTO;

//TODO:comments
public interface TokenVerificationService {
    VerifyResponseDTO verify(VerifyRequestDTO verifyRequestDTO);

    SignUpResponseDTO createUser(SignUpRequestDTO signUpRequestDTO);
}
