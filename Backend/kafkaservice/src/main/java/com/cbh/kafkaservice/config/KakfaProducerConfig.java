package com.cbh.kafkaservice.config;

import java.time.Duration;
import java.util.Properties;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class for creating Kafka producers with resilience
 * capabilities.
 *
 * @param <T> The type of the Kafka message value.
 */
@Slf4j
public class KakfaProducerConfig<T> {

	/**
	 * Gets default Kafka producer properties.
	 *
	 * @return Default Kafka producer properties.
	 */
	private static Properties getDefaultProperties() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.RETRIES_CONFIG, 3);
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
		props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
		props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transactional-id");
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "client-1");
		props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
		props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transactional-id");
		return props;
	}

	private static final CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom().slidingWindowSize(1)
			.minimumNumberOfCalls(1).failureRateThreshold(1).waitDurationInOpenState(Duration.ofSeconds(10))
			.slidingWindowType(SlidingWindowType.COUNT_BASED).build();

	private static final CircuitBreaker circuitBreaker = CircuitBreaker.of("kafkaCircuitBreaker", circuitBreakerConfig);

	/**
	 * Creates a default Kafka producer with predefined properties.
	 *
	 * @param <T> The type of the Kafka message value.
	 * @return Default Kafka producer instance.
	 * @throws KafkaException If an error occurs while creating the Kafka producer.
	 */
	public static <T> KafkaProducer<String, T> createDefaultProducer() throws KafkaException {
		Properties props = getDefaultProperties();
		try {
			KafkaProducer<String, T> defaultProducer = new KafkaProducer<>(props);
			printProducerProperties(props);
			return defaultProducer;
		} catch (Exception e) {
			log.error("Failed to create Kafka producer.", e);
			throw new KafkaException("Failed to create Kafka producer.", e);
		}
	}

	/**
	 * Sends a Kafka message with resilience handling using CircuitBreaker.
	 *
	 * @param <K>              The type of the Kafka message key.
	 * @param <V>              The type of the Kafka message value.
	 * @param producer         The Kafka producer instance.
	 * @param producerRecord   The Kafka producer record to be sent.
	 * @param responseCallback Callback to handle the response.
	 */
	public static <K, V> void sendMessage(KafkaProducer<K, V> producer, ProducerRecord<K, V> producerRecord,
			KafkaResponseCallback<V> responseCallback) {
		circuitBreaker.executeRunnable(() -> producer.send(producerRecord, (metadata,
				exception) -> handleCompletion(metadata, exception, producerRecord.value(), responseCallback)));

		if (circuitBreaker.getState() == CircuitBreaker.State.OPEN) {
			log.warn("Circuit breaker is open. Fallback behavior executed.");
		}
	}

	/**
	 * Handles the completion of a Kafka message send operation.
	 *
	 * @param <T>              The type of the Kafka message value.
	 * @param metadata         The metadata for the record that was sent (i.e., the
	 *                         partition and offset).
	 * @param exception        The exception thrown during message send, or null if
	 *                         it was successful.
	 * @param ob               The Kafka message value.
	 * @param responseCallback Callback to handle the response.
	 */
	private static <T> void handleCompletion(RecordMetadata metadata, Exception exception, T ob,
			KafkaResponseCallback<T> responseCallback) {
		if (exception == null) {
			log.info("Message sent successfully to topic: {}, partition: {}, offset: {}", metadata.topic(),
					metadata.partition(), metadata.offset());
			responseCallback.accept(metadata.topic(), ob);
		} else {
			log.error("Failed to send Kafka message", exception);
		}
	}

	/**
	 * Closes the given Kafka producer.
	 *
	 * @param <T>      The type of the Kafka message value.
	 * @param producer The Kafka producer to be closed.
	 */
	public void closeProducer(KafkaProducer<String, T> producer) {
		producer.close();
	}

	/**
	 * Prints Kafka producer properties.
	 *
	 * @param props The Kafka producer properties to be printed.
	 */
	public static void printProducerProperties(Properties props) {
		for (String propertyName : props.stringPropertyNames()) {
			String propertyValue = props.getProperty(propertyName);
			log.error("Producer properties {}", propertyValue);
		}
	}
}
