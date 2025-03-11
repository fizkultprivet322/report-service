package com.example.demo.service;

import com.example.demo.entity.ReportRequest;
import com.example.demo.entity.ReportResult;
import com.example.demo.messaging.KafkaProducerService;
import com.example.demo.repository.ReportRequestRepository;
import com.example.demo.repository.ReportResultRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRequestRepository requestRepository;
    private final ReportResultRepository resultRepository;
    private final KafkaProducerService kafkaProducerService;

    @Transactional
    public UUID createReport(ReportRequest request) {
        requestRepository.save(request);
        kafkaProducerService.sendMessage("Создан отчет с ID: " + request.getId());
        return request.getId();
    }
    public Optional<ReportResult> getReport(UUID id) {
        return resultRepository.findByRequestId(id);
    }
}