package com.crafters.DataService.services.Impl;

import com.crafters.DataService.dtos.SignUpRequestDTO;
import com.crafters.DataService.dtos.SignUpResponseDTO;
import com.crafters.DataService.dtos.VerifyRequestDTO;
import com.crafters.DataService.dtos.VerifyResponseDTO;
import com.crafters.DataService.entities.User;
import com.crafters.DataService.repositories.UserRepository;
import com.crafters.DataService.services.TokenVerificationService;
import com.crafters.DataService.utils.Impl.JwtUtilsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TokenVerificationServiceImpl implements TokenVerificationService {

    private final UserRepository userRepository;
    private final JwtUtilsImpl jwtService;
    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    public TokenVerificationServiceImpl(UserRepository userRepository, JwtUtilsImpl jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    //TODO:Add expiration time also to the response
    @Override
    public VerifyResponseDTO verify(VerifyRequestDTO verifyRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(verifyRequestDTO.getEmail(), verifyRequestDTO.getPassword()));
        User user = userRepository.findByEmail(verifyRequestDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        var jwt = jwtService.generateToken(user);
        return VerifyResponseDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .accessToken(jwt)
                .role(user.getRole())
                .build();
    }

    @Override
    public SignUpResponseDTO createUser(SignUpRequestDTO signUpRequestDTO) {
        //TODO: validation
        User user = User.builder()
                .name(signUpRequestDTO.getName())
                .email(signUpRequestDTO.getEmail())
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .role(signUpRequestDTO.getRole()).build();
        User savedUser = userRepository.save(user);
        return SignUpResponseDTO.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }
}
