package org.reportservice.notification.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {
    @RabbitListener(queues = "notification.queue")
    public void handleReportCompleted(String message) {
        if (message.startsWith("REPORT_COMPLETED:")) {
            String reportId = message.split(":")[1];
            sendUserNotification(reportId);
        }
    }

    private void sendUserNotification(String reportId) {
        System.out.println("[INFO] Получен отчет: " + reportId);
    }
}
