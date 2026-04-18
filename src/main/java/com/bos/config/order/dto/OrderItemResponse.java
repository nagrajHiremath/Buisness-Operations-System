package com.bos.config.order.dto;

import com.bos.config.common.enums.OrderStatus;
import com.bos.config.common.enums.OrderType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderItemResponse {

    private Long orderItemId;
    private Long catalogItemId;
    private String itemName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
    private String notes;
}
