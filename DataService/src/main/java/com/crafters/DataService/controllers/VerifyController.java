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

//TODO: logging,comments
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class VerifyController {

    private TokenVerificationServiceImpl tokenVerificationService;


    @PostMapping("/verify")
    public ResponseEntity<VerifyResponseDTO> verifyUser(@RequestBody VerifyRequestDTO verifyRequestDTO) {
        return new ResponseEntity<>(tokenVerificationService.verify(verifyRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<SignUpResponseDTO> createUser(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        return new ResponseEntity<>(tokenVerificationService.createUser(signUpRequestDTO), HttpStatus.CREATED);
    }
}
