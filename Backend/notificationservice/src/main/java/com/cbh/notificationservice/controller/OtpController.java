package com.cbh.notificationservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cbh.notificationservice.requestdto.EmailOtpRequestDto;
import com.cbh.notificationservice.responsedto.EmailOtpResponseDto;
import com.cbh.notificationservice.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@Tag(name = "Notification Controller", description = "API for Notification controller")
public class OtpController {

	private final OtpService otpService;

	@Operation(summary = "Send OTP to Email")
	@PostMapping("/send/otp")
	ResponseEntity<EmailOtpResponseDto> sendOtpToEmail(@RequestBody EmailOtpRequestDto emailOtpRequestDto) {
		
		return ResponseEntity.ok(otpService.sendOtpToEmail(emailOtpRequestDto));
	}

	@Operation(summary = "Verify Email OTP")
	@GetMapping("/verify/otp/{email}/{emailOtp}")
	ResponseEntity<Boolean> verifyEmailOtp(@PathVariable String email, @PathVariable Integer emailOtp) {
		return ResponseEntity.ok(otpService.isOtpValid(email, emailOtp));
	}

}
