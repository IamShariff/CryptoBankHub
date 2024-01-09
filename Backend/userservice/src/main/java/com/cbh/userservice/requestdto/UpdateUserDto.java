package com.cbh.userservice.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

	@NotBlank
	private String name;
	@Size(min = 10, max = 10, message = "Mobile number should be of 10 digit")
	private String mobileNumber;
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date of birth format. Use yyyy-MM-dd.")
	private String dob;
	@Size(min = 6)
	private String password;
	@NotBlank
	private String permanentAddress;
	@NotBlank
	private String city;
	@Size(min = 6, max = 6)
	private String pinCode;

}