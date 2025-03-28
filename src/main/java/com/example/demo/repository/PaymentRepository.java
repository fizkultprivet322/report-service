package com.example.demo.repository;

import com.example.demo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for {@link Payment} entity operations.
 * <p>
 * Provides CRUD operations and custom query methods for accessing payment data.
 * Extends {@link JpaRepository} to inherit standard JPA repository functionality.
 *
 * <p>Includes custom query methods for:
 * <ul>
 *   <li>Finding payments by product, layout and date range</li>
 * </ul>
 *
 * @see JpaRepository
 * @see Payment
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Finds all payments for a specific product and layout within a date range.
     * <p>
     * Returns a list of payments that match the given product ID, layout ID,
     * and fall within the specified time period (inclusive of boundaries).
     *
     * @param productId the ID of the product to filter by
     * @param layoutId the ID of the layout to filter by
     * @param startDate the start of the date range (inclusive)
     * @param endDate the end of the date range (inclusive)
     * @return list of matching payments, empty list if none found
     *
     * @see Payment
     */
    List<Payment> findByProductIdAndLayoutIdAndTimestampBetween(
            String productId,
            String layoutId,
            LocalDateTime startDate,
            LocalDateTime endDate);
}