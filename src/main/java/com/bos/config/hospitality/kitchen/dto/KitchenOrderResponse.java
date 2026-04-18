package com.bos.config.hospitality.kitchen.dto;

import com.bos.config.common.enums.OrderStatus;
import com.bos.config.common.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KitchenOrderResponse {

    private Long orderId;
    private String orderNumber;
    private String tableNumber;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private String notes;
    private List<KitchenOrderItemResponse> items;
}