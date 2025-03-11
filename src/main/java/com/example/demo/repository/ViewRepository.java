package com.example.demo.repository;

import com.example.demo.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ViewRepository extends JpaRepository<View, Long> {
    List<View> findByProductIdAndLayoutIdAndTimestampBetween(String productId, String layoutId, LocalDateTime startDate, LocalDateTime endDate);
}