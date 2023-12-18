package com.crafters.DataService.services.Impl;

import com.crafters.DataService.entities.User;
import com.crafters.DataService.repositories.UserRepository;
import com.crafters.DataService.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

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
    /**
     * Retrieves a user based on the specified user ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The User object associated with the provided user ID.
     * @throws ResponseStatusException If no user is found with the specified ID, a NOT_FOUND status exception is thrown.
     */
    @Override
    public User getUserById(String userId) {
        // TODO Auto-generated method stub
        return userRepository.findById(userId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,( "User by this "+userId+" doesn't exists")));
    }


}
