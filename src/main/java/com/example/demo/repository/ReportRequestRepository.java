package com.example.demo.repository;

import com.example.demo.entity.ReportRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReportRequestRepository extends JpaRepository<ReportRequest, UUID> {
}