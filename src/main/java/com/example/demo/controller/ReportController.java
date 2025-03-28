package com.example.demo.controller;

import com.example.demo.entity.ReportRequest;
import com.example.demo.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for managing report operations.
 *
 * <p>Provides endpoints for creating and retrieving reports with role-based access control.
 *
 * <p>Supported operations include:
 * <ul>
 *   <li>Creating new reports (USER role required)</li>
 *   <li>Retrieving existing reports (USER or ADMIN role required)</li>
 *   <li>Admin-specific operations (ADMIN role required)</li>
 * </ul>
 *
 * @see RestController
 * @see RequestMapping
 * @see ReportService
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * Creates a new report based on the provided request.
     *
     * <p>Requires USER role authentication.
     *
     * @param request the report creation request containing report data
     * @return ResponseEntity containing the generated report ID
     *
     * @see ReportRequest
     * @see PreAuthorize
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> createReport(@RequestBody ReportRequest request) {
        UUID reportId = reportService.createReport(request);
        return ResponseEntity.ok("Report ID: " + reportId);
    }

    /**
     * Retrieves a report by its unique identifier.
     *
     * <p>Requires either USER or ADMIN role authentication.
     *
     * @param id the UUID of the report to retrieve
     * @return ResponseEntity containing the report if found, or 404 if not found
     *
     * @see PreAuthorize
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getReport(@PathVariable UUID id) {
        return reportService.getReport(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Admin-only endpoint for special report operations.
     *
     * <p>Requires ADMIN role authentication.
     *
     * @return ResponseEntity with admin confirmation message
     *
     * @see PreAuthorize
     */
    @GetMapping("/admin/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminEndpoint() {
        return ResponseEntity.ok("This is an admin-only endpoint");
    }
}