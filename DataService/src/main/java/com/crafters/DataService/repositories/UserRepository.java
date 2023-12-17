package com.crafters.DataService.repositories;

import com.crafters.DataService.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repository interface for performing CRUD operations on the "users" collection in MongoDB.
 * Provides methods to interact with user data, including finding users by email.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user to find.
     * @return An Optional containing the user with the specified email, or empty if not found.
     */
    Optional<User> findByEmail(String email);

   /**
     * Check if a user with the given email already exists.
     *
     * @param email The email address to check for existence.
     * @return {@code true} if a user with the given email exists, {@code false} otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Check if a user with the given name already exists.
     *
     * @param name The name to check for existence.
     * @return {@code true} if a user with the given name exists, {@code false} otherwise.
     */
    boolean existsByName(String name);
    
}
