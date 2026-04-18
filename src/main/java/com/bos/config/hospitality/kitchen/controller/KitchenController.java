package com.bos.config.hospitality.kitchen.controller;

import com.bos.config.hospitality.kitchen.dto.KitchenOrderResponse;
import com.bos.config.hospitality.kitchen.dto.KitchenOrderStatusUpdateRequest;
import com.bos.config.hospitality.kitchen.service.KitchenService;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/kitchen")
@RequiredArgsConstructor
public class KitchenController {

    private final KitchenService kitchenService;

    @GetMapping("/orders")
    public ResponseEntity<List<KitchenOrderResponse>> getActiveKitchenOrders() {
        List<KitchenOrderResponse> orders = kitchenService.getActiveKitchenOrders();
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/orders/{orderId}/status")
    public ResponseEntity<KitchenOrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody KitchenOrderStatusUpdateRequest request) {
        KitchenOrderResponse updatedOrder = kitchenService.updateOrderStatus(orderId, request);
        return ResponseEntity.ok(updatedOrder);
    }
}