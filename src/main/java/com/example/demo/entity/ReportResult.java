package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Represents the computed results of a report generation process.
 * <p>
 * This entity stores analytical data calculated during report processing,
 * associated with a specific report request through the request ID.
 * Contains key performance metrics for business analysis.
 *
 * @see Entity
 * @see Getter
 * @see Setter
 * @see NoArgsConstructor
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ReportResult {

    /**
     * The unique identifier of the associated report request.
     * <p>
     * Serves as both primary key and foreign key reference to the
     * original {@link ReportRequest}. This 1:1 relationship ensures
     * each report request has exactly one set of results.
     */
    @Id
    private UUID requestId;

    /**
     * The calculated conversion ratio metric.
     * <p>
     * Represents the percentage or ratio of successful conversions
     * measured by the report. Typically expressed as a decimal value
     * between 0 and 1 (e.g., 0.85 for 85% conversion rate).
     */
    private Double conversionRatio;

    /**
     * The total count of payments processed.
     * <p>
     * Indicates the absolute number of payment transactions
     * that were included in the report's analysis period.
     */
    private Integer paymentCount;
}