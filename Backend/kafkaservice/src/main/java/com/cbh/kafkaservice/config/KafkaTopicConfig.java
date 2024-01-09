package com.cbh.kafkaservice.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Configuration class for managing Kafka topics.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaTopicConfig {

	/**
	 * Creates a new Kafka topic with the specified properties if it does not
	 * already exist.
	 *
	 * @param topicName   The name of the Kafka topic to be created.
	 * @param partitions  The number of partitions for the Kafka topic.
	 * @param replication The replication factor for the Kafka topic.
	 */
	public static void createTopic(String topicName, int partitions, int replication) {
		Properties properties = new Properties();
		properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

		try (AdminClient adminClient = AdminClient.create(properties)) {
			ListTopicsOptions options = new ListTopicsOptions().listInternal(true);
			Set<String> existingTopics = adminClient.listTopics(options).names().get();

			if (existingTopics.contains(topicName)) {
				log.info("Topic already exists: " + topicName);
			} else {
				NewTopic newTopic = new NewTopic(topicName, partitions, (short) replication); // replication factor
				adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
				log.info("Topic created successfully: " + topicName);
			}
		} catch (ExecutionException e) {
			log.error("Error creating or checking topic: " + e.getMessage());
		} catch (InterruptedException e) {
			log.error("Error creating or checking topic: " + e.getMessage());
		}
	}
}
