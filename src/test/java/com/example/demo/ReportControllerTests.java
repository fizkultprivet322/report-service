package com.example.demo;

import com.example.demo.controller.ReportController;
import com.example.demo.dto.ReportResultDto;
import com.example.demo.entity.ReportRequest;
import com.example.demo.entity.ReportResult;
import com.example.demo.mapper.ReportMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for {@link ReportController}.
 * <p>
 * Contains MockMvc-based tests that verify the REST API endpoints
 * for report generation and retrieval. Tests cover both successful
 * scenarios and error cases.
 *
 * <p>Tested endpoints:
 * <ul>
 *   <li>POST /api/reports - Report creation</li>
 *   <li>GET /api/reports/{id} - Report retrieval</li>
 * </ul>
 *
 * <p>Test scenarios:
 * <ul>
 *   <li>Successful report creation</li>
 *   <li>Successful report retrieval</li>
 *   <li>Report not found case</li>
 * </ul>
 *
 * @see ExtendWith
 * @see Mock
 * @see InjectMocks
 * @see Test
 */
@ExtendWith(MockitoExtension.class)
public class ReportControllerTests {

    @Mock
    private ReportService reportService;

    @Mock
    private ReportMapper reportMapper;

    @InjectMocks
    private ReportController reportController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    /**
     * Initializes test environment before each test.
     * <p>
     * Sets up:
     * <ul>
     *   <li>MockMvc instance for controller testing</li>
     *   <li>ObjectMapper for JSON serialization/deserialization</li>
     * </ul>
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
        objectMapper = new ObjectMapper();
    }

    /**
     * Tests successful report creation via POST /api/reports.
     * <p>
     * Verifies:
     * <ul>
     *   <li>HTTP 200 status code</li>
     *   <li>Correct response body format</li>
     *   <li>Proper service method invocation</li>
     *   <li>JSON request/response handling</li>
     * </ul>
     *
     * @throws Exception if mockMvc.perform fails
     */
    @Test
    void createReport_ShouldReturnReportId() throws Exception {
        UUID reportId = UUID.randomUUID();
        ReportRequest request = new ReportRequest();
        request.setProductId("product1");
        request.setLayoutId("layout1");

        when(reportService.createReport(any(com.example.demo.dto.ReportRequestDto.class))).thenReturn(reportId);

        mockMvc.perform(post("/api/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportId").value(reportId.toString()));
    }

    /**
     * Tests successful report retrieval via GET /api/reports/{id}.
     * <p>
     * Verifies:
     * <ul>
     *   <li>HTTP 200 status code</li>
     *   <li>Correct JSON response structure</li>
     *   <li>All expected report fields</li>
     *   <li>Proper service method invocation</li>
     * </ul>
     *
     * @throws Exception if mockMvc.perform fails
     */
    @Test
    void getReport_ShouldReturnReport() throws Exception {
        UUID reportId = UUID.randomUUID();
        ReportResult reportResult = new ReportResult();
        reportResult.setRequestId(reportId);
        reportResult.setConversionRatio(0.5);
        reportResult.setPaymentCount(10);

        ReportResultDto reportResultDto = new ReportResultDto();
        reportResultDto.setRequestId(reportId);
        reportResultDto.setConversionRatio(0.5);
        reportResultDto.setPaymentCount(10);

        when(reportService.getReport(reportId)).thenReturn(Optional.of(reportResult));
        when(reportMapper.toDto(reportResult)).thenReturn(reportResultDto);

        mockMvc.perform(get("/api/reports/{id}", reportId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value(reportId.toString()))
                .andExpect(jsonPath("$.conversionRatio").value(0.5))
                .andExpect(jsonPath("$.paymentCount").value(10));
    }

    /**
     * Tests report not found scenario via GET /api/reports/{id}.
     * <p>
     * Verifies:
     * <ul>
     *   <li>HTTP 404 status code</li>
     *   <li>Proper handling of missing reports</li>
     *   <li>Empty Optional handling from service</li>
     * </ul>
     *
     * @throws Exception if mockMvc.perform fails
     */
    @Test
    void getReport_ShouldReturnNotFound() throws Exception {
        UUID reportId = UUID.randomUUID();

        when(reportService.getReport(reportId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reports/{id}", reportId))
                .andExpect(status().isNotFound());
    }
}