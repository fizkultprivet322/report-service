package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a report generation request entity in the system.
 * <p>
 * This JPA entity stores information about report generation requests,
 * including processing status and temporal parameters. The entity features:
 * <ul>
 *   <li>UUID-based primary key generation</li>
 *   <li>Optimistic locking support</li>
 *   <li>Default status initialization</li>
 *   <li>Time-bound report parameters</li>
 * </ul>
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
public class ReportRequest {

    /**
     * Universally Unique Identifier (UUID) for this report request.
     * <p>
     * Generated using Hibernate's UUIDGenerator strategy. The column
     * is defined as a PostgreSQL/UUID type and is not updatable.
     *
     * @see GeneratedValue
     * @see GenericGenerator
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    /**
     * Identifier of the product associated with this report.
     * <p>
     * References a specific product in the product catalog.
     */
    private String productId;

    /**
     * Identifier of the layout configuration for this report.
     * <p>
     * Specifies the visual/layout template to be used when generating the report.
     */
    private String layoutId;

    /**
     * The inclusive start date/time for report data.
     * <p>
     * Defines the beginning of the time period covered by the report.
     */
    private LocalDateTime startDate;

    /**
     * The exclusive end date/time for report data.
     * <p>
     * Defines the end of the time period covered by the report.
     */
    private LocalDateTime endDate;

    /**
     * Current processing status of the report request.
     * <p>
     * Initialized to {@link ReportStatus#PENDING} by default.
     * Tracks the lifecycle of report generation process.
     */
    private ReportStatus status = ReportStatus.PENDING;

    /**
     * Version field for optimistic locking.
     * <p>
     * Automatically managed by JPA to prevent concurrent modification.
     * Incremented on each update to the entity.
     *
     * @see Version
     */
    @Version
    private Long version;
}