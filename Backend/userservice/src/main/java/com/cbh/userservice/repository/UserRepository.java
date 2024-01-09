package com.cbh.userservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cbh.userservice.models.User;

/**
 * Repository interface for managing user entities in MongoDB.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Finds a user by their email.
     *
     * @param email The email of the user to be found.
     * @return An Optional containing the user if found, or an empty Optional otherwise.
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their user ID.
     *
     * @param userId The user ID of the user to be found.
     * @return An Optional containing the user if found, or an empty Optional otherwise.
     */
    Optional<User> findByUserId(String userId);
}
