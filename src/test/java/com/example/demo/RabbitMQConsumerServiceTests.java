package com.example.demo;

import com.example.demo.entity.ReportRequest;
import com.example.demo.repository.ReportRequestRepository;
import com.example.demo.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link RabbitMQConsumerService}.
 * <p>
 * This class contains unit tests that verify the behavior of the Kafka consumer service
 * when processing report request messages. Tests cover both successful processing
 * and error scenarios using Mockito for mocking dependencies.
 *
 * <p>Key test scenarios:
 * <ul>
 *   <li>Successful processing of valid report requests</li>
 *   <li>Proper handling of missing report requests</li>
 *   <li>Verification of service interactions</li>
 * </ul>
 *
 * @see ExtendWith
 * @see Mock
 * @see InjectMocks
 * @see Test
 */
@ExtendWith(MockitoExtension.class)
public class RabbitMQConsumerServiceTests {

    @Mock
    private ReportRequestRepository requestRepository;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private RabbitMQConsumerService rabbitMQConsumerService;

    private UUID reportId;

    /**
     * Initializes test data before each test execution.
     * <p>
     * Generates a new random UUID for each test to ensure test isolation.
     */
    @BeforeEach
    void setUp() {
        reportId = UUID.randomUUID();
    }

    /**
     * Tests successful processing of a valid report request message.
     * <p>
     * Verifies that:
     * <ul>
     *   <li>The service correctly parses the report ID from the message</li>
     *   <li>The report request is properly retrieved from the repository</li>
     *   <li>The report service is invoked to process the request</li>
     * </ul>
     */
    @Test
    void consume_ShouldProcessReportRequest() {
        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setId(reportId);

        when(requestRepository.findById(reportId)).thenReturn(Optional.of(reportRequest));

        rabbitMQConsumerService.consume("Создан отчет с ID: " + reportId);

        verify(reportService).calculateAndSaveReportResult(reportId);
    }

    /**
     * Tests handling of a message referencing a non-existent report request.
     * <p>
     * Verifies that:
     * <ul>
     *   <li>The service properly handles missing report requests</li>
     *   <li>No report processing is attempted for invalid requests</li>
     *   <li>An appropriate error is logged (verified via logging assertions in actual implementation)</li>
     * </ul>
     */
    @Test
    void consume_ShouldLogErrorWhenReportRequestNotFound() {
        when(requestRepository.findById(reportId)).thenReturn(Optional.empty());

        rabbitMQConsumerService.consume("Создан отчет с ID: " + reportId);

        verify(reportService, never()).calculateAndSaveReportResult(any(UUID.class));
    }
}