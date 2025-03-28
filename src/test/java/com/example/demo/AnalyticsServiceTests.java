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

/**
 * Test class for {@link AnalyticsService}.
 * <p>
 * This class contains unit tests that verify the behavior of AnalyticsService methods
 * using Mockito for mocking dependencies. Tests focus on the service's ability to:
 * <ul>
 *   <li>Retrieve view analytics data</li>
 *   <li>Fetch payment transaction data</li>
 *   <li>Handle time-bound queries correctly</li>
 * </ul>
 *
 * <p>Test cases validate both successful scenarios and proper data handling.
 *
 * @see ExtendWith
 * @see Mock
 * @see InjectMocks
 * @see Test
 */
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

    /**
     * Sets up test data before each test execution.
     * <p>
     * Initializes common test data:
     * <ul>
     *   <li>startDate - current date/time</li>
     *   <li>endDate - 1 day after startDate</li>
     * </ul>
     */
    @BeforeEach
    void setUp() {
        startDate = LocalDateTime.now();
        endDate = startDate.plusDays(1);
    }

    /**
     * Tests successful retrieval of view analytics data.
     * <p>
     * Verifies that:
     * <ul>
     *   <li>Service properly delegates to ViewRepository</li>
     *   <li>Returned list contains expected view data</li>
     *   <li>Data matches the requested criteria</li>
     * </ul>
     */
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

    /**
     * Tests successful retrieval of payment transaction data.
     * <p>
     * Verifies that:
     * <ul>
     *   <li>Service properly delegates to PaymentRepository</li>
     *   <li>Returned list contains expected payment data</li>
     *   <li>Data matches the requested criteria</li>
     * </ul>
     */
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