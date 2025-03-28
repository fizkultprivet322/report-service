package com.example.demo.repository;

import com.example.demo.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for {@link View} entity operations.
 * <p>
 * Provides data access methods for view tracking analytics, including standard CRUD operations
 * and temporal queries for analyzing user viewing patterns. Uses {@link Long} as the
 * identifier type for view records.
 *
 * <p>Key features:
 * <ul>
 *   <li>Inherits standard JPA repository operations from {@link JpaRepository}</li>
 *   <li>Provides time-bound queries for analytical purposes</li>
 *   <li>Supports product and layout-specific view analysis</li>
 * </ul>
 *
 * @see JpaRepository
 * @see View
 */
public interface ViewRepository extends JpaRepository<View, Long> {

    /**
     * Finds all view records for a specific product and layout within a date range.
     * <p>
     * This query is particularly useful for:
     * <ul>
     *   <li>Analyzing viewing trends over time</li>
     *   <li>Comparing layout performance</li>
     *   <li>Generating time-based usage reports</li>
     * </ul>
     *
     * @param productId the product identifier to filter by
     * @param layoutId the specific layout variant to filter by
     * @param startDate the start of the date range (inclusive)
     * @param endDate the end of the date range (inclusive)
     * @return list of matching view records, empty list if none found
     *
     * @throws IllegalArgumentException if any date parameter is null
     */
    List<View> findByProductIdAndLayoutIdAndTimestampBetween(
            String productId,
            String layoutId,
            LocalDateTime startDate,
            LocalDateTime endDate);
}