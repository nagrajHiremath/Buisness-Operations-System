package com.bos.config.hospitality.kitchen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KitchenOrderItemResponse {

    private String itemName;
    private Integer quantity;
    private String notes;
}