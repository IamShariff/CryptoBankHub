package com.cbh.userservice.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoKycDto {

	@Email
	private String email;
	@Size(min = 12, max = 12, message = "Invalid aadhar card")
	private String aadharCard;
	@Size(min = 8, max = 18, message = "Invalid pan card")
	private String panCard;

}
