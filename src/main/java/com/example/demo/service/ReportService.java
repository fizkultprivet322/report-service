package com.example.demo.service;

import com.example.demo.entity.ReportRequest;
import com.example.demo.entity.ReportResult;
import com.example.demo.entity.ReportStatus;
import com.example.demo.messaging.KafkaProducerService;
import com.example.demo.repository.ReportRequestRepository;
import com.example.demo.repository.ReportResultRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRequestRepository requestRepository;
    private final ReportResultRepository resultRepository;
    private final KafkaProducerService kafkaProducerService;
    private final AnalyticsService analyticsService;

    @Transactional
    public UUID createReport(ReportRequest request) {
        requestRepository.save(request);
        kafkaProducerService.sendMessage("Создан отчет с ID: " + request.getId());
        return request.getId();
    }

    public Optional<ReportResult> getReport(UUID id) {
        return resultRepository.findByRequestId(id);
    }

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