package com.cbh.notificationservice.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
	
	private String fieldName;
	private String message;
	private boolean status;

}
