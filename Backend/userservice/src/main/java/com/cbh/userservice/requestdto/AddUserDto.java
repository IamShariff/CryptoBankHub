package com.cbh.userservice.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data class representing the information needed to add a new user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddUserDto {

	@NotBlank
	private String name;
	@NotBlank
	@Email
	private String email;
	@Size(min = 10, max = 10, message = "Mobile number should be of 10 digit")
	private String mobileNumber;
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date of birth format. Use yyyy-MM-dd.")
	private String dob;
	@Size(min = 6, message = "Password should be atleast 6 digit")
	private String password;
	@NotBlank
	private String permanentAddress;
	@NotBlank
	private String city;
	@Size(min = 6, max = 6)
	private String pinCode;
}
