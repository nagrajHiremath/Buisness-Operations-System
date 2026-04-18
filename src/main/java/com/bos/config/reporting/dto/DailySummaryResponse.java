package com.bos.config.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySummaryResponse {
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private Long totalPaidInvoices;
    private Long pendingPayments;
    private BigDecimal cashCollection;
    private BigDecimal upiCollection;
    private BigDecimal cardCollection;
}