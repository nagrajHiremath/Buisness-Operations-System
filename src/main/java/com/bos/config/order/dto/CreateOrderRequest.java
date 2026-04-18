package com.bos.config.order.dto;

import java.util.List;

import com.bos.config.common.enums.OrderType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequest {

    @NotNull(message = "Order type is required")
    private OrderType orderType;   // DINE_IN / PARCEL

    // list of items in order
    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<CreateOrderItemRequest> items;

    // generic notes (optional)
    private String notes;

    // separate hospitality context (scalable design)
    private HospitalityOrderContextRequest context;
}