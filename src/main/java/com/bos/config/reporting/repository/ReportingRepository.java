package com.bos.config.reporting.repository;

import com.bos.config.reporting.dto.DailySummaryResponse;
import com.bos.config.reporting.dto.OrdersSummaryResponse;
import com.bos.config.reporting.dto.TopItemResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReportingRepository {

    DailySummaryResponse getDailySummary(LocalDate date);

    OrdersSummaryResponse getOrdersSummary(LocalDate date);

    List<TopItemResponse> getTopItems(LocalDate date);
}