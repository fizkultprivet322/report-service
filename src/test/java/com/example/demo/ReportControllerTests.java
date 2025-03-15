package com.example.demo;

import com.example.demo.controller.ReportController;
import com.example.demo.entity.ReportRequest;
import com.example.demo.entity.ReportResult;
import com.example.demo.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTests {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createReport_ShouldReturnReportId() throws Exception {
        UUID reportId = UUID.randomUUID();
        ReportRequest request = new ReportRequest();
        request.setProductId("product1");
        request.setLayoutId("layout1");

        when(reportService.createReport(any(ReportRequest.class))).thenReturn(reportId);

        mockMvc.perform(post("/api/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Report ID: " + reportId));
    }

    @Test
    void getReport_ShouldReturnReport() throws Exception {
        UUID reportId = UUID.randomUUID();
        ReportResult reportResult = new ReportResult();
        reportResult.setRequestId(reportId);
        reportResult.setConversionRatio(0.5);
        reportResult.setPaymentCount(10);

        when(reportService.getReport(reportId)).thenReturn(Optional.of(reportResult));

        mockMvc.perform(get("/api/reports/{id}", reportId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value(reportId.toString()))
                .andExpect(jsonPath("$.conversionRatio").value(0.5))
                .andExpect(jsonPath("$.paymentCount").value(10));
    }

    @Test
    void getReport_ShouldReturnNotFound() throws Exception {
        UUID reportId = UUID.randomUUID();

        when(reportService.getReport(reportId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reports/{id}", reportId))
                .andExpect(status().isNotFound());
    }
}