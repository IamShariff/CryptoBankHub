package com.cbh.userservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kyc {
	
	private String aadharCard;
	private String panCard;

	
}
