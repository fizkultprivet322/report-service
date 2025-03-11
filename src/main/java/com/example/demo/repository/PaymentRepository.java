package com.example.demo.repository;

import com.example.demo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByProductIdAndLayoutIdAndTimestampBetween(String productId, String layoutId, LocalDateTime startDate, LocalDateTime endDate);
}