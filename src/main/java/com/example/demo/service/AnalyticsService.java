package com.example.demo.service;

import com.example.demo.entity.Payment;
import com.example.demo.entity.View;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.ViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final ViewRepository viewRepository;
    private final PaymentRepository paymentRepository;

    public List<View> getViews(String productId, String layoutId, LocalDateTime startDate, LocalDateTime endDate) {
        return viewRepository.findByProductIdAndLayoutIdAndTimestampBetween(productId, layoutId, startDate, endDate);
    }

    public List<Payment> getPayments(String productId, String layoutId, LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByProductIdAndLayoutIdAndTimestampBetween(productId, layoutId, startDate, endDate);
    }
}