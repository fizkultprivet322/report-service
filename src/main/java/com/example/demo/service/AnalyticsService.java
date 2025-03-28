package com.example.demo.service;

import com.example.demo.entity.Payment;
import com.example.demo.entity.View;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.ViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for handling analytical data operations.
 * <p>
 * Provides business logic for retrieving and analyzing view and payment data
 * for specific products and layouts within defined time periods. Acts as a
 * facade between controllers and repository layer for analytics operations.
 *
 * <p>Key responsibilities:
 * <ul>
 *   <li>Retrieving view analytics data</li>
 *   <li>Fetching payment transaction data</li>
 *   <li>Providing time-bound analytical queries</li>
 *   <li>Serving as an abstraction layer for analytical operations</li>
 * </ul>
 *
 * @see Service
 * @see View
 * @see Payment
 */
@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final ViewRepository viewRepository;
    private final PaymentRepository paymentRepository;

    /**
     * Retrieves view records for a specific product and layout within a date range.
     * <p>
     * Returns all view events that match the specified criteria, useful for:
     * <ul>
     *   <li>User engagement analysis</li>
     *   <li>Layout performance comparison</li>
     *   <li>Traffic pattern evaluation</li>
     * </ul>
     *
     * @param productId the product identifier to filter by
     * @param layoutId the layout variant to filter by
     * @param startDate the start of the date range (inclusive)
     * @param endDate the end of the date range (inclusive)
     * @return list of matching view records, empty list if none found
     * @throws IllegalArgumentException if any date parameter is null
     */
    public List<View> getViews(String productId, String layoutId,
                               LocalDateTime startDate, LocalDateTime endDate) {
        return viewRepository.findByProductIdAndLayoutIdAndTimestampBetween(
                productId, layoutId, startDate, endDate);
    }

    /**
     * Retrieves payment records for a specific product and layout within a date range.
     * <p>
     * Returns all payment transactions that match the specified criteria, useful for:
     * <ul>
     *   <li>Revenue analysis</li>
     *   <li>Conversion rate calculation</li>
     *   <li>Sales performance evaluation</li>
     * </ul>
     *
     * @param productId the product identifier to filter by
     * @param layoutId the layout variant to filter by
     * @param startDate the start of the date range (inclusive)
     * @param endDate the end of the date range (inclusive)
     * @return list of matching payment records, empty list if none found
     * @throws IllegalArgumentException if any date parameter is null
     */
    public List<Payment> getPayments(String productId, String layoutId,
                                     LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByProductIdAndLayoutIdAndTimestampBetween(
                productId, layoutId, startDate, endDate);
    }
}