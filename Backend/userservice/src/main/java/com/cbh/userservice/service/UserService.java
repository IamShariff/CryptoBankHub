package com.cbh.userservice.service;

import com.cbh.userservice.models.User;
import com.cbh.userservice.requestdto.AddUserDto;
import com.cbh.userservice.requestdto.DoKycDto;

/**
 * Service interface for managing user-related operations.
 */
public interface UserService {

    /**
     * Adds a new user to the system.
     *
     * @param addUser The information of the user to be added.
     * @return The user object representing the added user.
     */
    User addUser(AddUserDto addUser);

    /**
     * Performs KYC (Know Your Customer) for a user.
     *
     * @param doKyc The KYC information to be processed.
     * @return The user object representing the user after KYC.
     */
    User kyc(DoKycDto doKyc);

    /**
     * Retrieves a user by their user ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The user object representing the user with the specified ID.
     */
    User fetchUserById(String userId);

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address of the user to retrieve.
     * @return The user object representing the user with the specified email address.
     */
    User fetchUserByEmail(String email);
    
    String getKycStatus(String userId);
}

