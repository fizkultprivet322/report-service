package com.example.demo.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ReportIdResponse {
    private UUID reportId;

    public ReportIdResponse(UUID reportId) {
        this.reportId = reportId;
    }
}
