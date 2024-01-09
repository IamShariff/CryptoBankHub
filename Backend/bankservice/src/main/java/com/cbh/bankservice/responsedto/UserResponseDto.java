package com.cbh.bankservice.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

	private String userId;
	private String name;
	private String email;
	private String mobileNumber;
	private String kycStatus;

}
