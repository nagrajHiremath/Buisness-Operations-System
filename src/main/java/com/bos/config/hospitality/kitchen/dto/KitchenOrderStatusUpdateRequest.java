package com.bos.config.hospitality.kitchen.dto;

import com.bos.config.common.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KitchenOrderStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private OrderStatus status;
}