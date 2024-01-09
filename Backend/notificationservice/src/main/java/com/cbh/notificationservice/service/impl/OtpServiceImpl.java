package com.cbh.notificationservice.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import org.springframework.stereotype.Service;
import com.cbh.exceptionservice.exception.ExpiredException;
import com.cbh.exceptionservice.exception.NotFoundException;
import com.cbh.exceptionservice.exception.UserBlockedException;
import com.cbh.notificationservice.model.Otp;
import com.cbh.notificationservice.repository.OtpRepository;
import com.cbh.notificationservice.requestdto.EmailOtpRequestDto;
import com.cbh.notificationservice.responsedto.EmailOtpResponseDto;
import com.cbh.notificationservice.service.JavamailService;
import com.cbh.notificationservice.service.OtpService;
import com.cbh.notificationservice.util.Constant;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OtpServiceImpl implements OtpService {

	private final OtpRepository otpRepository;
	private final JavamailService javamailService;
	private static final int OTP_LENGTH = 6;

	@Override
	public void saveOtp(String email, Integer generatedOTP) {
		otpRepository.findByEmail(email).ifPresentOrElse(existingOtp -> {
			// Update existing user's OTP details
			existingOtp.setGeneratedOtp(encryptOtp(generatedOTP));
			existingOtp.setExpired(false);
			existingOtp.setFailedAttempts(0);
			existingOtp.setCreatedTime(Instant.now());
			otpRepository.save(existingOtp);
		}, () -> {
			// Create a new OTP entry for the user
			Otp otp = new Otp();
			otp.setEmail(email);
			otp.setExpired(false);
			otp.setGeneratedOtp(encryptOtp(generatedOTP));
			otp.setCreatedTime(Instant.now());
			otp.setBlocked(false);
			otp.setFailedAttempts(0);
			otpRepository.save(otp);
		});
	}

	@Override
	public boolean isOtpExpired(Instant createdTime) {
		Instant now = Instant.now();
		Duration duration = Duration.between(createdTime, now);

		long differenceInSeconds = duration.getSeconds();
		return differenceInSeconds > 300; // OTP expires after 300 seconds (5 minutes)
	}

	@Override
	public boolean userAlreadyGeneratedOtp(String email) {
		return otpRepository.findByEmail(email).map(otp -> {
			Instant now = Instant.now();
			Duration duration = Duration.between(otp.getCreatedTime(), now);
			return duration.getSeconds() < 120;
		}).orElse(false);
	}

	@Override
	public boolean isUserBlocked(String email) {
		return otpRepository.findByEmail(email).map(Otp::isBlocked).orElse(false);
	}

	@Override
	public Integer generateOTP(String email) {
		if (isUserBlocked(email)) {
			throw new UserBlockedException(Constant.FIELD_EMAIL, Constant.MESSAGE_ACCOUNT_BLOCKED);
		}
		if (userAlreadyGeneratedOtp(email)) {
			throw new NotFoundException(Constant.FIELD_EMAIL, Constant.MESSAGE_WAIT_FOR_OTP);
		}
		SecureRandom random = new SecureRandom();
		Integer newlygeneratedOtp = random.ints(OTP_LENGTH, 0, 10).reduce(0, (otp, digit) -> otp * 10 + digit);
		saveOtp(email, newlygeneratedOtp);
		return newlygeneratedOtp;
	}

	@Override
	public String encryptOtp(Integer generatedOTP) {
		try {
			String otpString = String.valueOf(generatedOTP);
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashedBytes = digest.digest(otpString.getBytes(StandardCharsets.UTF_8));

			return bytesToHex(hashedBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new NotFoundException("Error encrypting OTP", e.getMessage());
		}
	}

	private String bytesToHex(byte[] bytes) {
		StringBuilder stringBuilder = new StringBuilder();
		for (byte b : bytes) {
			stringBuilder.append(String.format("%02x", b));
		}
		return stringBuilder.toString();
	}

	@Override
	public boolean isOtpValid(String email, Integer emailOtp) {
		String encryptedEnteredOtp = encryptOtp(emailOtp);
		boolean isOtpValid;
		Otp user = otpRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email", "Not found"));

		if (user.isBlocked()) {
			throw new UserBlockedException(Constant.FIELD_OTP, Constant.MESSAGE_ACCOUNT_BLOCKED);
		}

		int failedAttempts = user.getFailedAttempts();

		if (failedAttempts < 3) {
			if (isOtpExpired(user.getCreatedTime())) {
				user.setExpired(true);
				otpRepository.save(user);
				throw new ExpiredException(Constant.FIELD_OTP, Constant.MESSAGE_EXPIRED_OTP);
			}

			failedAttempts++;

			if (!encryptedEnteredOtp.equals(user.getGeneratedOtp())) {
				user.setFailedAttempts(failedAttempts);
				otpRepository.save(user);
				throw new NotFoundException(Constant.FIELD_OTP,
						Constant.MESSAGE_INCORRECT_OTP + (3 - failedAttempts) + Constant.MESSAGE_ATTEMPTS_REMAINING);
			} else {
				isOtpValid = true;
				user.setFailedAttempts(0);
				otpRepository.save(user);
			}
		} else {
			user.setBlocked(true);
			otpRepository.save(user);
			throw new UserBlockedException(Constant.FIELD_EMAIL, Constant.MESSAGE_ACCOUNT_BLOCKED);
		}

		return isOtpValid;
	}

	@Override
	public EmailOtpResponseDto sendOtpToEmail(EmailOtpRequestDto emailOtpRequestDto) {
		boolean isEmailSent;
		Integer generatedOTP = generateOTP(emailOtpRequestDto.email());
		String subject = Constant.EMAIL_TYPE.getOrDefault(emailOtpRequestDto.emailType(), "Default Subject");
		javamailService.sendEmail(emailOtpRequestDto.email(), subject, generatedOTP);
		isEmailSent = true;
		return new EmailOtpResponseDto(emailOtpRequestDto.email(), isEmailSent);
	}

}
