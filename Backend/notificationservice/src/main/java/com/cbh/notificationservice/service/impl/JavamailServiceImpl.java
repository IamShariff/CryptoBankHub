package com.cbh.notificationservice.service.impl;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.cbh.notificationservice.service.JavamailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JavamailServiceImpl implements JavamailService {

	private final JavaMailSender mailSender;

	public void sendEmail(String recipientEmail, String subject, Integer otp) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setTo(recipientEmail);
			helper.setSubject(subject);
			String emailBody = String.format("Your 6 digit Otp is : %s", otp);
			helper.setText(emailBody, true);
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendNotification(String recipientEmail, String subject, String text) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setTo(recipientEmail);
			helper.setText(text);
			helper.setSubject(subject);
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
