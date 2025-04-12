package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReportUpdateDto {
    private String productId;
    private String layoutId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
