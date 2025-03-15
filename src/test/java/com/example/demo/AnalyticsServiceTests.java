package com.example.demo;

import com.example.demo.entity.Payment;
import com.example.demo.entity.View;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.ViewRepository;
import com.example.demo.service.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceTests {

    @Mock
    private ViewRepository viewRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        startDate = LocalDateTime.now();
        endDate = startDate.plusDays(1);
    }

    @Test
    void getViews_ShouldReturnViews() {
        View view = new View();
        view.setProductId("product1");
        view.setLayoutId("layout1");
        view.setTimestamp(startDate);

        when(viewRepository.findByProductIdAndLayoutIdAndTimestampBetween(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(view));

        List<View> views = analyticsService.getViews("product1", "layout1", startDate, endDate);

        assertEquals(1, views.size());
        assertEquals(view, views.get(0));
    }

    @Test
    void getPayments_ShouldReturnPayments() {
        Payment payment = new Payment();
        payment.setProductId("product1");
        payment.setLayoutId("layout1");
        payment.setTimestamp(startDate);

        when(paymentRepository.findByProductIdAndLayoutIdAndTimestampBetween(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(payment));

        List<Payment> payments = analyticsService.getPayments("product1", "layout1", startDate, endDate);

        assertEquals(1, payments.size());
        assertEquals(payment, payments.get(0));
    }
}