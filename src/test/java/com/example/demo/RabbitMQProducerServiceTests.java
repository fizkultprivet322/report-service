package com.example.demo;

import com.example.demo.messaging.RabbitMQProducerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link RabbitMQProducerService}.
 * <p>
 * Contains unit tests that verify the behavior of the Kafka producer service
 * when sending messages to the Kafka topic. Uses Mockito to mock the KafkaTemplate
 * and verify proper interactions.
 *
 * <p>Test scenarios:
 * <ul>
 *   <li>Successful message sending to the configured topic</li>
 * </ul>
 *
 * @see ExtendWith
 * @see Mock
 * @see InjectMocks
 * @see Test
 */
@ExtendWith(MockitoExtension.class)
public class RabbitMQProducerServiceTests {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private RabbitMQProducerService rabbitMQProducerService;

    /**
     * Tests that the service correctly sends messages to Kafka.
     * <p>
     * Verifies that:
     * <ul>
     *   <li>The message is sent to the correct topic ("report_requests")</li>
     *   <li>The message content is preserved</li>
     *   <li>The KafkaTemplate's send method is invoked exactly once</li>
     * </ul>
     */
    @Test
    void sendMessage_ShouldSendMessageToKafka() {
        String message = "Test message";

        rabbitMQProducerService.sendMessage(message);

        verify(kafkaTemplate).send(eq("report_requests"), eq(message));
    }
}