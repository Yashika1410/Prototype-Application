package com.crafters.DataService.validators;

import com.crafters.DataService.dtos.SignUpRequestDTO;
import com.crafters.DataService.exceptions.ValidationFailureException;
import com.crafters.DataService.repositories.UserRepository;
import org.springframework.util.StringUtils;
/**
 * Utility class for performing validation checks on SignUpRequestDTO.
 */
public class ValidationUtils {
    /**
     * Validates the provided SignUpRequestDTO for name, email, email format, and password strength.
     *
     * @param userRepository    The repository for user-related operations.
     * @param signUpRequestDTO  The SignUpRequestDTO to be validated.
     * @throws ValidationFailureException If any validation check fails.
     */

    public static void validateSignUpRequest(UserRepository userRepository, SignUpRequestDTO signUpRequestDTO) {
        validateNameAndEmail(signUpRequestDTO.getName(), signUpRequestDTO.getEmail());
        validateEmailIsNotRegistered(userRepository, signUpRequestDTO.getEmail());
        validateEmailFormat(signUpRequestDTO.getEmail());
        validatePasswordStrength(signUpRequestDTO.getPassword());

    }
    /**
     * Validates that the provided name and email are not empty.
     *
     * @param name  The name to validate.
     * @param email The email to validate.
     * @throws ValidationFailureException If either name or email is empty.
     */
    public static void validateNameAndEmail(String name, String email) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(email)) {
            throw new ValidationFailureException("Name and email cannot be empty");
        }
    }
    /**
     * Validates that the provided email is not already registered.
     *
     * @param userRepository The repository for user-related operations.
     * @param email          The email to validate.
     * @throws ValidationFailureException If the email is already in use.
     */
    public static void validateEmailIsNotRegistered(UserRepository userRepository, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ValidationFailureException("Email is already in use");
        }
    }
    /**
     * Validates the strength of the provided password.
     *
     * @param password The password to validate.
     * @throws ValidationFailureException If the password does not meet the required strength criteria.
     */
    public static void validatePasswordStrength(String password) {
        if (!isPasswordStrong(password)) {
            throw new ValidationFailureException("Password must be at least 8 characters long and contain a mix of uppercase and lowercase letters, numbers, and special characters");
        }
    }
    /**
     * Validates the format of the provided email.
     *
     * @param email The email to validate.
     * @throws ValidationFailureException If the email format is invalid.
     */
    public static void validateEmailFormat(String email) {
        if (!isValidEmail(email)) {
            throw new ValidationFailureException("Invalid email format");
        }
    }

    // Helper method to check password strength
    private static boolean isPasswordStrong(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordRegex);
    }

    // Helper method to check email format
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        return email.matches(emailRegex);
    }
}
