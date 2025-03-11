package com.example.demo.repository;

import com.example.demo.entity.ReportResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReportResultRepository extends JpaRepository<ReportResult, UUID> {
    Optional<ReportResult> findByRequestId(UUID requestId);
}