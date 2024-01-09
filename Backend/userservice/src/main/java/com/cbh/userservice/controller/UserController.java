package com.cbh.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cbh.userservice.models.User;
import com.cbh.userservice.requestdto.AddUserDto;
import com.cbh.userservice.requestdto.DoKycDto;
import com.cbh.userservice.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

/**
 * Controller class for managing user-related operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@ApiResponse(description = "Operations related to users")
public class UserController {

	private final UserService userService;

	/**
	 * Adds a new user.
	 *
	 * @param addUser The user information to be added.
	 * @return ResponseEntity containing the added user.
	 */
	@PostMapping()
	@Operation(tags = "Add a new user")
	public ResponseEntity<User> add(@RequestBody AddUserDto addUserDto) {
		return ResponseEntity.ok(userService.addUser(addUserDto));
	}

	/**
	 * Performs KYC (Know Your Customer) for a user.
	 *
	 * @param doKyc The KYC information to be processed.
	 * @return ResponseEntity containing the user after KYC.
	 */
	@PostMapping("/kyc")
	@Operation(tags = "Perform KYC for a user")
	public ResponseEntity<User> kyc(@RequestBody DoKycDto doKycDto) {
		return ResponseEntity.ok(userService.kyc(doKycDto));
	}

	/**
	 * Retrieves a user by their ID.
	 *
	 * @param userId The ID of the user to retrieve.
	 * @return ResponseEntity containing the user with the specified ID.
	 */
	@GetMapping("/{userId}")
	@Operation(tags = "Get user by ID")
	public ResponseEntity<User> userById(@PathVariable String userId) {
		return ResponseEntity.ok(userService.fetchUserById(userId));
	}

	@GetMapping("/kyc/{userId}")
	@Operation(tags = "Get kyc status")
	public ResponseEntity<String> kycStatus(@PathVariable String userId) {
		return ResponseEntity.ok(userService.getKycStatus(userId));
	}

}
