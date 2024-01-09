package com.cbh.notificationservice.service;

public interface JavamailService {
	
	public void sendEmail(String recipientEmail, String subject, Integer generatedEmailOtp);

	public void sendNotification(String recipientEmail, String subject, String message);
}
