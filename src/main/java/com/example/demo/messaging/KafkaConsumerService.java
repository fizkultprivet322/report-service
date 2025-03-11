package com.example.demo.messaging;

import com.example.demo.entity.ReportRequest;
import com.example.demo.entity.ReportResult;
import com.example.demo.repository.ReportRequestRepository;
import com.example.demo.repository.ReportResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final ReportRequestRepository requestRepository;
    private final ReportResultRepository resultRepository;

    @KafkaListener(topics = "report_requests", groupId = "report-group")
    public void consume(String message) {
        log.info("Получено сообщение: {}", message);

        // Извлеките ID отчета из сообщения
        UUID reportId = UUID.fromString(message.split(": ")[1]);

        // Найдите запрос отчета в базе данных
        Optional<ReportRequest> reportRequestOptional = requestRepository.findById(reportId);

        if (reportRequestOptional.isPresent()) {
            ReportRequest reportRequest = reportRequestOptional.get();

            // Создайте и сохраните результат отчета
            ReportResult reportResult = new ReportResult();
            reportResult.setRequestId(reportRequest.getId()); // Убедитесь, что requestId установлен правильно
            reportResult.setConversionRatio(0.0); // Пример значения
            reportResult.setPaymentCount(0); // Пример значения

            resultRepository.save(reportResult);

            log.info("Создан и сохранен результат отчета для запроса с ID: {}", reportRequest.getId());
        } else {
            log.error("Запрос отчета с ID {} не найден", reportId);
        }
    }
}
