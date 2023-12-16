package com.crafters.DataService.services.Impl;

import com.crafters.DataService.dtos.SignUpRequestDTO;
import com.crafters.DataService.dtos.SignUpResponseDTO;
import com.crafters.DataService.entities.User;
import com.crafters.DataService.repositories.UserRepository;
import com.crafters.DataService.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Implementation of the UserService interface, providing user-related operations.
 */
@Component
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    /**
     * Constructs a UserServiceImpl with the necessary dependencies.
     *
     * @param userRepository The repository for user data.
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /**
     * Load user details by username (email).
     *
     * @param username The username (email) for which to load user details.
     * @return UserDetails containing user information.
     * @throws UsernameNotFoundException if the user with the specified username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Invalid user details"));
    }


}
