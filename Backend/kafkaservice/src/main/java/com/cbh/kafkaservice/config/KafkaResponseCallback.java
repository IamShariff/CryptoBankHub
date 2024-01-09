package com.cbh.kafkaservice.config;

/**
 * A functional interface representing a callback for handling Kafka response.
 *
 * @param <T> The type of the Kafka response value.
 */
@FunctionalInterface
public interface KafkaResponseCallback<T> {

	/**
	 * Accepts the Kafka topic and value as part of the callback.
	 *
	 * @param topic The Kafka topic associated with the response.
	 * @param value The value of the Kafka response.
	 */
	void accept(String topic, T value);
}
