package com.bos.config.reporting.service;

import com.bos.config.reporting.dto.DailySummaryResponse;
import com.bos.config.reporting.dto.OrdersSummaryResponse;
import com.bos.config.reporting.dto.TopItemResponse;
import com.bos.config.reporting.repository.ReportingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportingService {

    private final ReportingRepository reportingRepository;

    public DailySummaryResponse getDailySummary() {
        return reportingRepository.getDailySummary(LocalDate.now());
    }

    public OrdersSummaryResponse getOrdersSummary() {
        return reportingRepository.getOrdersSummary(LocalDate.now());
    }

    public List<TopItemResponse> getTopItems() {
        return reportingRepository.getTopItems(LocalDate.now());
    }
}