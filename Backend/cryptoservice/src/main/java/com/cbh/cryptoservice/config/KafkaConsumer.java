package com.cbh.cryptoservice.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.cbh.cryptoservice.responsedto.TransactionFromPaymentResponseDto;
import com.cbh.cryptoservice.service.CryptoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

	private final ObjectMapper objectMapper;
	private final CryptoService cryptoService;

	@KafkaListener(topics = "CryptoPortfolio", groupId = "update")
	public void listenTransactionFromPayment(ConsumerRecord<String, String> message) {
		try {

			TransactionFromPaymentResponseDto transaction = objectMapper.readValue(message.value(),
					TransactionFromPaymentResponseDto.class);

			cryptoService.updatePortfolio(transaction);
			log.info("Received data in listenTransactionFromPayment Service: {}", transaction.toString());

		} catch (JsonProcessingException e) {
			log.error("Error processing JSON message: {}", e.getMessage());
		}
	}
}
