package com.example.demo.controller;

import com.example.demo.dto.ReportIdResponse;
import com.example.demo.dto.ReportRequestDto;
import com.example.demo.dto.ReportResultDto;
import com.example.demo.entity.ReportRequest;
import com.example.demo.entity.ReportResult;
import com.example.demo.mapper.ReportMapper;
import com.example.demo.service.ReportService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Reports", description = "Report API")
public class ReportController {

    private final ReportService reportService;
    private final ReportMapper reportMapper;

    /**
     * Creates a new report based on the provided request.
     *
     * <p>Requires USER role authentication.
     *
     * @param requestDto the report creation request containing report data
     * @return ResponseEntity containing the generated report ID
     *
     * @see ReportRequest
     * @see PreAuthorize
     */
    @Operation(
            summary = "Create new report",
            description = "Creates report generation request and returns its ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Report created successfully",
                            content = @Content(schema = @Schema(example = "Report ID: d3d94468-2d6a-4d2a-95a3-6ad246b8c5a0"))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request data",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content)
            })
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReportIdResponse> createReport(@RequestBody ReportRequestDto requestDto) {
        UUID reportId = reportService.createReport(requestDto);
        return ResponseEntity.ok(new ReportIdResponse(reportId));
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
    @Operation(
            summary = "Get report by ID",
            description = "Returns generated report if available",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Report found",
                            content = @Content(schema = @Schema(implementation = ReportResult.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Report not found",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content)
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ReportResultDto> getReport(@PathVariable UUID id) {
        return reportService.getReport(id)
                .map(reportMapper::toDto)
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
    @Operation(
            summary = "Admin endpoint",
            description = "Accessible only to users with ADMIN role",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful access",
                            content = @Content(schema = @Schema(example = "This is an admin-only endpoint"))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied",
                            content = @Content)
            })
    @GetMapping("/admin/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminEndpoint() {
        return ResponseEntity.ok("This is an admin-only endpoint");
    }
}