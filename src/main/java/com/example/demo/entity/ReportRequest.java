package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ReportRequest {
    @Id
    @GeneratedValue
    private UUID id;

    private String productId;
    private String layoutId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ReportStatus status = ReportStatus.PENDING;
    @Version
    private Long version;
}

