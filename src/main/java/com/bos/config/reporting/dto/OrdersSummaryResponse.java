package com.bos.config.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersSummaryResponse {
    private Long totalOrders;
    private Long preparingOrders;
    private Long readyOrders;
    private Long cancelledOrders;
    private Long completedOrders;
}