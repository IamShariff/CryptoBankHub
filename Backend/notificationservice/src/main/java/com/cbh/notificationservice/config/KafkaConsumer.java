package com.cbh.notificationservice.config;

import java.nio.charset.StandardCharsets;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import com.cbh.notificationservice.service.JavamailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

	private final JavamailService javamailService;

	/**
	 * Listen to the testKafka topic for incoming messages.
	 *
	 * @param message The Kafka message received.
	 */
	@KafkaListener(topics = "userNotifications", groupId = "notifyUsers")
	public void listenSendNotificationToEmail(ConsumerRecord<String, String> message) {

		String email = null;
		String subject = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Headers headers = message.headers();

			for (Header header : headers) {
				String headerKey = header.key();
				byte[] headerValueBytes = header.value();
				String headerValue = new String(headerValueBytes, StandardCharsets.UTF_8);
				if ("email".equals(headerKey)) {
					email = headerValue;
					log.info("Received email: {}", email);
				} else if ("subject".equals(headerKey)) {
					subject = headerValue;
					log.info("Received subject: {}", subject);
				}
			}
			String readValue = objectMapper.readValue(message.value(), String.class);

			javamailService.sendNotification(email, subject, readValue);

			log.info("Received data in Notification Service: {}", readValue);

		} catch (JsonProcessingException e) {
			log.error("Error processing JSON message: {}", e.getMessage());
		}
	}

}