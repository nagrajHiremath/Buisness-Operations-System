package com.bos.config.order.dto;

import com.bos.config.common.enums.OrderStatus;
import com.bos.config.common.enums.OrderType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {

    private Long orderId;
    private Long businessId;
    private String orderNumber;
    private OrderType orderType;
    private OrderStatus status;

    // hospitality-specific for now
    private String tableNumber;

    private BigDecimal subtotalAmount;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;

    private String notes;

    private Long createdByUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<OrderItemResponse> items;

    // optional separate context for future scalable design
    private HospitalityOrderContextResponse context;
}
