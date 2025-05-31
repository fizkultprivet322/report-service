package com.example.demo.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RabbitMQProducerService {
    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange reportExchange;

    
    public void sendReportCompletedNotification(UUID reportId) {
        String message = "REPORT_COMPLETED:" + reportId;
        rabbitTemplate.convertAndSend(
                reportExchange.getName(),
                "notification.routing.key",
                message
        );
        System.out.println("Sent notification for report: " + reportId);
    }
}