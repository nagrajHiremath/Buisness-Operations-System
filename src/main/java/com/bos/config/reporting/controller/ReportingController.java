package com.bos.config.reporting.controller;

import com.bos.config.reporting.dto.DailySummaryResponse;
import com.bos.config.reporting.dto.OrdersSummaryResponse;
import com.bos.config.reporting.dto.TopItemResponse;
import com.bos.config.reporting.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportingController {

    private final ReportingService reportingService;

    @GetMapping("/daily-summary")
    public ResponseEntity<DailySummaryResponse> getDailySummary() {
        DailySummaryResponse summary = reportingService.getDailySummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/orders-summary")
    public ResponseEntity<OrdersSummaryResponse> getOrdersSummary() {
        OrdersSummaryResponse summary = reportingService.getOrdersSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/top-items")
    public ResponseEntity<List<TopItemResponse>> getTopItems() {
        List<TopItemResponse> topItems = reportingService.getTopItems();
        return ResponseEntity.ok(topItems);
    }
}