package com.example.demo.service;

import com.example.demo.dto.ReportRequestDto;
import com.example.demo.entity.ReportRequest;
import com.example.demo.entity.ReportResult;
import com.example.demo.entity.ReportStatus;
import com.example.demo.mapper.ReportMapper;
import com.example.demo.messaging.KafkaProducerService;
import com.example.demo.repository.ReportRequestRepository;
import com.example.demo.repository.ReportResultRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing report generation and processing.
 * <p>
 * Handles the complete report lifecycle including creation, calculation,
 * and retrieval of report results. Coordinates with analytics services
 * and messaging systems for asynchronous processing.
 *
 * <p>Key responsibilities:
 * <ul>
 *   <li>Report request creation and tracking</li>
 *   <li>Asynchronous report processing initiation</li>
 *   <li>Report result calculation and persistence</li>
 *   <li>Report status management</li>
 *   <li>Report data retrieval</li>
 * </ul>
 *
 * @see Service
 * @see Transactional
 */
@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRequestRepository requestRepository;
    private final ReportResultRepository resultRepository;
    private final KafkaProducerService kafkaProducerService;
    private final AnalyticsService analyticsService;
    private final ReportMapper reportMapper;

    /**
     * Creates a new report request and initiates processing.
     * <p>
     * Persists the report request and sends a message to Kafka
     * to trigger asynchronous report generation.
     *
     * @param requestDto the report request containing parameters
     * @return UUID of the created report request
     * @throws IllegalArgumentException if the request is null
     */
    @Transactional
    public UUID createReport(ReportRequestDto requestDto) {
        ReportRequest reportRequest = reportMapper.toEntity(requestDto);
        requestRepository.save(reportRequest);
        kafkaProducerService.sendMessage("Report ID: " + reportRequest.getId());
        return reportRequest.getId();
    }

    /**
     * Retrieves a report result by its associated request ID.
     * <p>
     * Returns the calculated report data if available.
     *
     * @param id UUID of the report request
     * @return Optional containing the report result if found
     */
    @Transactional
    public Optional<ReportResult> getReport(UUID id) {
        return resultRepository.findByRequestId(id);
    }

    /**
     * Calculates and persists report results.
     * <p>
     * Performs the following operations:
     * <ol>
     *   <li>Retrieves the report request</li>
     *   <li>Calculates view and payment metrics</li>
     *   <li>Computes conversion ratio</li>
     *   <li>Saves the report results</li>
     *   <li>Updates the request status to COMPLETED</li>
     * </ol>
     *
     * @param reportId UUID of the report to process
     * @throws RuntimeException if the report request is not found
     */
    @Transactional
    public void calculateAndSaveReportResult(UUID reportId) {
        ReportRequest reportRequest = requestRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report request not found"));

        String productId = reportRequest.getProductId();
        String layoutId = reportRequest.getLayoutId();
        LocalDateTime startDate = reportRequest.getStartDate();
        LocalDateTime endDate = reportRequest.getEndDate();

        int viewCount = analyticsService.getViews(productId, layoutId, startDate, endDate).size();
        int paymentCount = analyticsService.getPayments(productId, layoutId, startDate, endDate).size();

        double conversionRatio = (viewCount == 0) ? 0 : (double) paymentCount / viewCount;

        ReportResult reportResult = new ReportResult();
        reportResult.setRequestId(reportId);
        reportResult.setConversionRatio(conversionRatio);
        reportResult.setPaymentCount(paymentCount);

        resultRepository.save(reportResult);

        reportRequest.setStatus(ReportStatus.COMPLETED);
        requestRepository.save(reportRequest);
    }
}