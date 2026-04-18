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
public class TopItemResponse {
    private String itemName;
    private Long totalQuantitySold;
    private BigDecimal totalRevenueGenerated;
}