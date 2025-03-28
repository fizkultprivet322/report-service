package com.example.demo.repository;

import com.example.demo.entity.ReportResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for {@link ReportResult} entity operations.
 * <p>
 * Provides data access capabilities for report results, using {@link UUID} as the identifier type.
 * Includes both standard CRUD operations from {@link JpaRepository} and custom query methods.
 *
 * <p>Key features:
 * <ul>
 *   <li>Standard JPA repository operations</li>
 *   <li>Custom query method to find results by associated request ID</li>
 *   <li>Optional return type for safe handling of missing results</li>
 * </ul>
 *
 * @see JpaRepository
 * @see ReportResult
 * @see Optional
 */
public interface ReportResultRepository extends JpaRepository<ReportResult, UUID> {

    /**
     * Finds a report result by its associated request ID.
     * <p>
     * Retrieves the computed result for a specific report request if it exists.
     * The method returns an {@link Optional} to safely handle cases where no result exists
     * for the given request ID.
     *
     * @param requestId the UUID of the associated report request
     * @return an Optional containing the matching report result if found,
     *         or empty Optional if no result exists for the request
     *
     * @see ReportResult
     * @see Optional
     */
    Optional<ReportResult> findByRequestId(UUID requestId);
}