package com.example.demo.mapper;

import com.example.demo.dto.ReportRequestDto;
import com.example.demo.dto.ReportResultDto;
import com.example.demo.entity.ReportRequest;
import com.example.demo.entity.ReportResult;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public ReportRequest toEntity(ReportRequestDto dto) {
        ReportRequest entity = new ReportRequest();
        entity.setProductId(dto.getProductId());
        entity.setLayoutId(dto.getLayoutId());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        return entity;
    }

    public ReportResultDto toDto(ReportResult entity) {
        ReportResultDto dto = new ReportResultDto();
        dto.setRequestId(entity.getRequestId());
        dto.setConversionRatio(entity.getConversionRatio());
        dto.setPaymentCount(entity.getPaymentCount());
        return dto;
    }
}
