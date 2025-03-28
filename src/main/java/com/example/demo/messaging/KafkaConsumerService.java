package com.example.demo.messaging;

import com.example.demo.entity.ReportRequest;
import com.example.demo.repository.ReportRequestRepository;
import com.example.demo.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service for consuming and processing report requests from Kafka.
 * <p>
 * This service listens to the 'report_requests' Kafka topic and handles incoming
 * report generation messages. It coordinates the report calculation process
 * and manages the report lifecycle.
 *
 * <p>Key responsibilities:
 * <ul>
 *   <li>Listening to Kafka messages on the configured topic</li>
 *   <li>Processing report request messages</li>
 *   <li>Initiating report calculation</li>
 *   <li>Handling success/error cases with appropriate logging</li>
 * </ul>
 *
 * @see Service
 * @see KafkaListener
 * @see Slf4j
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final ReportRequestRepository requestRepository;
    private final ReportService reportService;

    /**
     * Consumes messages from the 'report_requests' Kafka topic.
     * <p>
     * Processes incoming messages containing report request IDs by:
     * <ol>
     *   <li>Parsing the report ID from the message</li>
     *   <li>Fetching the corresponding report request</li>
     *   <li>Initiating report calculation if request exists</li>
     *   <li>Logging appropriate success/error messages</li>
     * </ol>
     *
     * @param message The raw Kafka message in format "Report ID: {UUID}"
     * @throws IllegalArgumentException if the message format is invalid
     * @see KafkaListener
     */
    @KafkaListener(topics = "report_requests", groupId = "report-group")
    public void consume(String message) {
        log.info("Получено сообщение: {}", message);

        UUID reportId = UUID.fromString(message.split(": ")[1]);

        Optional<ReportRequest> reportRequestOptional = requestRepository.findById(reportId);

        if (reportRequestOptional.isPresent()) {
            ReportRequest reportRequest = reportRequestOptional.get();

            reportService.calculateAndSaveReportResult(reportId);

            log.info("Создан и сохранен результат отчета для запроса с ID: {}", reportRequest.getId());
        } else {
            log.error("Запрос отчета с ID {} не найден", reportId);
        }
    }
}