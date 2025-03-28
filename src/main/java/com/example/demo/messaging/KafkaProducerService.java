package com.example.demo.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for producing report request messages to Kafka.
 * <p>
 * This service handles sending messages to the 'report_requests' Kafka topic,
 * enabling asynchronous processing of report generation requests.
 *
 * <p>Key features:
 * <ul>
 *   <li>Simple interface for sending report request messages</li>
 *   <li>Pre-configured with the target Kafka topic</li>
 *   <li>Uses Spring's KafkaTemplate for message production</li>
 * </ul>
 *
 * @see Service
 * @see KafkaTemplate
 */
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "report_requests";

    /**
     * Sends a message to the report requests topic.
     * <p>
     * The message should contain all necessary information to identify
     * and process the report request. Typically this would be a report ID
     * or a serialized request object.
     *
     * @param message the content to be sent to Kafka topic
     * @see KafkaTemplate#send(String, Object)
     */
    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}