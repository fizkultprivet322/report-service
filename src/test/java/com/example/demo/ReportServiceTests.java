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

    @Test
    void createReport_ShouldSaveRequestAndSendMessage() {
        when(requestRepository.save(any(ReportRequest.class))).thenReturn(reportRequest);

        UUID result = reportService.createReport(reportRequest);

        assertEquals(reportRequest.getId(), result);
        verify(requestRepository).save(reportRequest);
        verify(kafkaProducerService).sendMessage("Создан отчет с ID: " + reportRequest.getId());
    }

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