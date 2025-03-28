package com.example.demo;

import com.example.demo.entity.ReportRequest;
import com.example.demo.entity.ReportResult;
import com.example.demo.entity.ReportStatus;
import com.example.demo.messaging.KafkaProducerService;
import com.example.demo.repository.ReportRequestRepository;
import com.example.demo.repository.ReportResultRepository;
import com.example.demo.service.AnalyticsService;
import com.example.demo.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ReportService}.
 * <p>
 * Contains unit tests that verify the business logic and behavior
 * of the report service, including report creation, retrieval,
 * and calculation processes.
 *
 * <p>Test scenarios:
 * <ul>
 *   <li>Report request creation and Kafka message sending</li>
 *   <li>Report result retrieval</li>
 *   <li>Report calculation and status update</li>
 * </ul>
 *
 * @see ExtendWith
 * @see Mock
 * @see InjectMocks
 * @see Test
 */
@ExtendWith(MockitoExtension.class)
public class ReportServiceTests {

    @Mock
    private ReportRequestRepository requestRepository;

    @Mock
    private ReportResultRepository resultRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks
    private ReportService reportService;

    private ReportRequest reportRequest;

    /**
     * Initializes test data before each test execution.
     * <p>
     * Creates a sample report request with:
     * <ul>
     *   <li>Random UUID</li>
     *   <li>Sample product and layout IDs</li>
     *   <li>Current date as start date</li>
     *   <li>Next day as end date</li>
     *   <li>PENDING status</li>
     * </ul>
     */
    @BeforeEach
    void setUp() {
        reportRequest = new ReportRequest();
        reportRequest.setId(UUID.randomUUID());
        reportRequest.setProductId("product1");
        reportRequest.setLayoutId("layout1");
        reportRequest.setStartDate(LocalDateTime.now());
        reportRequest.setEndDate(LocalDateTime.now().plusDays(1));
        reportRequest.setStatus(ReportStatus.PENDING);
    }

    /**
     * Tests successful report request creation.
     * <p>
     * Verifies that:
     * <ul>
     *   <li>The report request is saved to the repository</li>
     *   <li>A Kafka message is sent with the report ID</li>
     *   <li>The correct report ID is returned</li>
     * </ul>
     */
    @Test
    void createReport_ShouldSaveRequestAndSendMessage() {
        when(requestRepository.save(any(ReportRequest.class))).thenReturn(reportRequest);

        UUID result = reportService.createReport(reportRequest);

        assertEquals(reportRequest.getId(), result);
        verify(requestRepository).save(reportRequest);
        verify(kafkaProducerService).sendMessage("Создан отчет с ID: " + reportRequest.getId());
    }

    /**
     * Tests successful report result retrieval.
     * <p>
     * Verifies that:
     * <ul>
     *   <li>The service returns the correct report result</li>
     *   <li>The repository is queried with the correct ID</li>
     *   <li>Optional contains the expected result</li>
     * </ul>
     */
    @Test
    void getReport_ShouldReturnReportResult() {
        UUID reportId = UUID.randomUUID();
        ReportResult reportResult = new ReportResult();
        reportResult.setRequestId(reportId);
        reportResult.setConversionRatio(0.5);
        reportResult.setPaymentCount(10);

        when(resultRepository.findByRequestId(reportId)).thenReturn(Optional.of(reportResult));

        Optional<ReportResult> result = reportService.getReport(reportId);

        assertTrue(result.isPresent());
        assertEquals(reportResult, result.get());
    }

    /**
     * Tests report calculation and result saving.
     * <p>
     * Verifies that:
     * <ul>
     *   <li>The report request is properly retrieved</li>
     *   <li>Analytics data is collected for the correct period</li>
     *   <li>The report result is saved</li>
     *   <li>The request status is updated to COMPLETED</li>
     * </ul>
     */
    @Test
    void calculateAndSaveReportResult_ShouldSaveResultAndUpdateStatus() {
        when(requestRepository.findById(reportRequest.getId())).thenReturn(Optional.of(reportRequest));
        when(analyticsService.getViews(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());
        when(analyticsService.getPayments(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        reportService.calculateAndSaveReportResult(reportRequest.getId());

        verify(resultRepository).save(any(ReportResult.class));
        verify(requestRepository).save(reportRequest);
        assertEquals(ReportStatus.COMPLETED, reportRequest.getStatus());
    }
}