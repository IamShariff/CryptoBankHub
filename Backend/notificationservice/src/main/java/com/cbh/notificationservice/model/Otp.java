package com.cbh.notificationservice.model;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Otp {

	@Id
	private String id;
	private String generatedOtp;
	private Instant createdTime;
	private String email;
	private boolean isExpired;
	private boolean isBlocked;
	private int failedAttempts;

}
