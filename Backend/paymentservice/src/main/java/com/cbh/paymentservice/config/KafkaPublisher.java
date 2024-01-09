package com.cbh.paymentservice.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Configuration;

import com.cbh.kafkaservice.config.KafkaResponseCallback;
import com.cbh.kafkaservice.config.KakfaProducerConfig;
import com.cbh.paymentservice.requestdto.CryptoTransactionEventDto;
import com.cbh.paymentservice.requestdto.KafkaNotificationRequestDto;

@Configuration
public class KafkaPublisher {

	public void sendTransactionToCryptoService(CryptoTransactionEventDto cryptoTransactionDto, String topic, String key,
			KafkaResponseCallback<Object> responseCallback) {

		KafkaProducer<String, Object> producer = KakfaProducerConfig.createDefaultProducer();

		producer.initTransactions();
		producer.beginTransaction();

		ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(topic, key, cryptoTransactionDto);

		KakfaProducerConfig.sendMessage(producer, producerRecord, responseCallback);

		producer.commitTransaction();
		producer.close();
	}

	public void sendNotificationToUser(KafkaNotificationRequestDto kafkaNotificationRequestDto,
			KafkaResponseCallback<Object> responseCallback) {

		KafkaProducer<String, Object> producer = KakfaProducerConfig.createDefaultProducer();

		producer.initTransactions();
		producer.beginTransaction();

		ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(kafkaNotificationRequestDto.getTopic(),
				kafkaNotificationRequestDto.getKey(), kafkaNotificationRequestDto.getMessage());

		Map<String, byte[]> headersMap = new HashMap<>();

		headersMap.put("email", kafkaNotificationRequestDto.getEmail().getBytes());
		headersMap.put("subject", kafkaNotificationRequestDto.getSubject().getBytes());

		headersMap.forEach((key, value) -> producerRecord.headers().add(key, value));

		KakfaProducerConfig.sendMessage(producer, producerRecord, responseCallback);

		producer.commitTransaction();
		producer.close();
	}
}
