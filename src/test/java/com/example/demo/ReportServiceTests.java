package com.example.demo;

import com.example.demo.dto.ReportRequestDto;
import com.example.demo.entity.ReportRequest;
import com.example.demo.entity.ReportResult;
import com.example.demo.entity.ReportStatus;
import com.example.demo.mapper.ReportMapper;
import com.example.demo.messaging.RabbitMQProducerService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTests {

    @Mock
    private ReportRequestRepository requestRepository;

    @Mock
    private ReportResultRepository resultRepository;

    @Mock
    private RabbitMQProducerService rabbitMQProducerService;

    @Mock
    private AnalyticsService analyticsService;

    @Mock
    private ReportMapper reportMapper;

    @InjectMocks
    private ReportService reportService;

    private ReportRequestDto reportRequestDto;
    private ReportRequest reportRequest;

    @BeforeEach
    void setUp() {
        reportRequestDto = new ReportRequestDto();
        reportRequestDto.setProductId("product1");
        reportRequestDto.setLayoutId("layout1");
        reportRequestDto.setStartDate(LocalDateTime.now());
        reportRequestDto.setEndDate(LocalDateTime.now().plusDays(1));

        reportRequest = new ReportRequest();
        reportRequest.setId(UUID.randomUUID());
        reportRequest.setProductId(reportRequestDto.getProductId());
        reportRequest.setLayoutId(reportRequestDto.getLayoutId());
        reportRequest.setStartDate(reportRequestDto.getStartDate());
        reportRequest.setEndDate(reportRequestDto.getEndDate());
        reportRequest.setStatus(ReportStatus.PENDING);
    }

    @Test
    void createReport_ShouldSaveRequestAndSendMessage() {
        when(reportMapper.toEntity(reportRequestDto)).thenReturn(reportRequest);
        when(requestRepository.save(any(ReportRequest.class))).thenReturn(reportRequest);

        UUID result = reportService.createReport(reportRequestDto);

        assertEquals(reportRequest.getId(), result);
        verify(reportMapper).toEntity(reportRequestDto);
        verify(requestRepository).save(reportRequest);
        verify(rabbitMQProducerService).sendMessage("Report ID: " + reportRequest.getId());
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