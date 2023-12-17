package com.crafters.DataService.services.Impl;

import com.crafters.DataService.dtos.SignUpRequestDTO;
import com.crafters.DataService.dtos.SignUpResponseDTO;
import com.crafters.DataService.dtos.VerifyRequestDTO;
import com.crafters.DataService.dtos.VerifyResponseDTO;
import com.crafters.DataService.entities.User;
import com.crafters.DataService.exceptions.ValidationFailureException;
import com.crafters.DataService.repositories.UserRepository;
import com.crafters.DataService.services.TokenVerificationService;
import com.crafters.DataService.utils.Impl.JwtUtilsImpl;
import com.crafters.DataService.validators.ValidationUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Implementation of the TokenVerificationService interface, providing
 * functionality
 * for user verification and creation of user accounts using JWT tokens.
 *
 * TODO: Add expiration time also to the response.
 */
@Component
public class TokenVerificationServiceImpl implements TokenVerificationService {

        private final UserRepository userRepository;
        private final JwtUtilsImpl jwtService;
        private final AuthenticationManager authenticationManager;

        private final PasswordEncoder passwordEncoder;

        /**
         * Constructs a TokenVerificationServiceImpl with the necessary dependencies.
         *
         * @param userRepository        The repository for user data.
         * @param jwtService            The service for JWT token operations.
         * @param authenticationManager The authentication manager for user
         *                              authentication.
         * @param passwordEncoder       The password encoder for encoding user
         *                              passwords.
         */
        public TokenVerificationServiceImpl(UserRepository userRepository, JwtUtilsImpl jwtService,
                        AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
                this.userRepository = userRepository;
                this.jwtService = jwtService;
                this.authenticationManager = authenticationManager;
                this.passwordEncoder = passwordEncoder;
        }

        /**
         * Verifies a user based on the provided verification request data.
         *
         * @param verifyRequestDTO The data transfer object containing information
         *                         needed for user verification.
         * @return VerifyResponseDTO containing user details and an access token upon
         *         successful verification.
         * @throws IllegalArgumentException if the provided email or password is
         *                                  invalid.
         */
        @Override
        public VerifyResponseDTO verify(VerifyRequestDTO verifyRequestDTO) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(verifyRequestDTO.getEmail(),
                                                verifyRequestDTO.getPassword()));
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

        /**
         * Creates a new user account based on the provided sign-up request data.
         *
         * TODO: Add validation for sign-up request data.
         *
         * @param signUpRequestDTO The data transfer object containing user registration
         *                         information.
         * @return SignUpResponseDTO containing the newly created user's details.
         */
        @Override
        public SignUpResponseDTO createUser(SignUpRequestDTO signUpRequestDTO) {

                ValidationUtils.validateSignUpRequest(userRepository, signUpRequestDTO);
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
