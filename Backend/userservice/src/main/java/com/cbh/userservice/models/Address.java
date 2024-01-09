package com.cbh.userservice.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Address {
	
	private String permanentAddress;
	private String city;
	private String pinCode;

}
