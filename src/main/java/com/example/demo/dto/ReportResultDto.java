package com.example.demo.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ReportResultDto {
    private UUID requestId;
    private Double conversionRatio;
    private Integer paymentCount;
}
