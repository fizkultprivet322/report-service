package com.example.demo.repository;

import com.example.demo.entity.ReportRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * JPA Repository for {@link ReportRequest} entities.
 * <p>
 * Provides CRUD operations and default JPA query methods for report requests.
 * Uses {@link UUID} as the identifier type for report requests.
 *
 * <p>This repository enables:
 * <ul>
 *   <li>Standard create/read/update/delete operations</li>
 *   <li>Built-in query methods from {@link JpaRepository}</li>
 *   <li>Custom query methods can be added as needed</li>
 * </ul>
 *
 * @see JpaRepository
 * @see ReportRequest
 */
public interface ReportRequestRepository extends JpaRepository<ReportRequest, UUID> {
}