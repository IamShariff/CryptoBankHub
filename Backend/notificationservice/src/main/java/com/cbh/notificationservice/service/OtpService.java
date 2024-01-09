package com.cbh.notificationservice.service;

import java.time.Instant;
import com.cbh.notificationservice.requestdto.EmailOtpRequestDto;
import com.cbh.notificationservice.responsedto.EmailOtpResponseDto;

public interface OtpService {

	boolean isOtpExpired(Instant createdTime);

	boolean userAlreadyGeneratedOtp(String email);

	boolean isUserBlocked(String email);

	String encryptOtp(Integer generatedOTP);

    Integer generateOTP(String email);

	void saveOtp(String email, Integer generatedEmailOtp);

	boolean isOtpValid(String email, Integer emailOtp);

	EmailOtpResponseDto sendOtpToEmail(EmailOtpRequestDto emailOtpRequestDto);

}
