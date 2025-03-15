package com.example.demo;

import com.example.demo.entity.ReportRequest;
import com.example.demo.messaging.KafkaConsumerService;
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

@ExtendWith(MockitoExtension.class)
public class KafkaConsumerServiceTests {

    @Mock
    private ReportRequestRepository requestRepository;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    private UUID reportId;

    @BeforeEach
    void setUp() {
        reportId = UUID.randomUUID();
    }

    @Test
    void consume_ShouldProcessReportRequest() {
        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setId(reportId);

        when(requestRepository.findById(reportId)).thenReturn(Optional.of(reportRequest));

        kafkaConsumerService.consume("Создан отчет с ID: " + reportId);

        verify(reportService).calculateAndSaveReportResult(reportId);
    }

    @Test
    void consume_ShouldLogErrorWhenReportRequestNotFound() {
        when(requestRepository.findById(reportId)).thenReturn(Optional.empty());

        kafkaConsumerService.consume("Создан отчет с ID: " + reportId);

        verify(reportService, never()).calculateAndSaveReportResult(any(UUID.class));
    }
}