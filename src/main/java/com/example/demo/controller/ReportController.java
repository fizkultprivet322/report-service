package com.example.demo.controller;

import com.example.demo.entity.ReportRequest;
import com.example.demo.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<String> createReport(@RequestBody ReportRequest request) {
        UUID reportId = reportService.createReport(request);
        return ResponseEntity.ok("Report ID: " + reportId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReport(@PathVariable UUID id) {
        return reportService.getReport(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}