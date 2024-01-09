package com.cbh.bankservice.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Configuration;
import com.cbh.bankservice.requestdto.KafkaNotificationRequestDto;

import com.cbh.kafkaservice.config.KafkaResponseCallback;
import com.cbh.kafkaservice.config.KakfaProducerConfig;

@Configuration
public class KafkaPublisher {

	public static void sendNotificationToUser(KafkaNotificationRequestDto kafkaNotificationRequestDto,
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
