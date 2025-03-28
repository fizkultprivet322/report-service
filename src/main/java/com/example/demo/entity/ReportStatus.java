package com.example.demo.entity;

/**
 * Represents the possible statuses of a report generation process.
 * <p>
 * This enum defines the lifecycle states for report requests in the system:
 * <ul>
 *   <li>{@link #PENDING} - Initial state when report is queued for processing</li>
 *   <li>{@link #COMPLETED} - Final state when report generation is finished</li>
 * </ul>
 *
 * <p>Used primarily by the {@link ReportRequest} entity to track report status.
 */
public enum ReportStatus {
    /**
     * The report request has been created but processing has not yet begun.
     * <p>
     * Reports in this status are awaiting available system resources
     * or scheduled processing time.
     */
    PENDING,

    /**
     * The report has been successfully generated and is ready for access.
     * <p>
     * Indicates all processing is complete and results are available
     * in the associated {@link ReportResult}.
     */
    COMPLETED
}