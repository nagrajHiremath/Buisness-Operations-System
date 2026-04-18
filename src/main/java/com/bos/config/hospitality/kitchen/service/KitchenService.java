package com.bos.config.hospitality.kitchen.service;

import com.bos.config.common.enums.OrderStatus;
import com.bos.config.common.enums.OrderType;
import com.bos.config.hospitality.kitchen.dto.KitchenOrderItemResponse;
import com.bos.config.hospitality.kitchen.dto.KitchenOrderResponse;
import com.bos.config.hospitality.kitchen.dto.KitchenOrderStatusUpdateRequest;
import com.bos.config.hospitality.kitchen.exceptions.InvalidKitchenStatusException;
import com.bos.config.order.entity.OrderEntity;
import com.bos.config.order.entity.OrderItemEntity;
import com.bos.config.order.exceptions.OrderNotFoundException;
import com.bos.config.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KitchenService {

    @Autowired
    private ModelMapper modelMapper;
    private final OrderRepository orderRepository;

    public List<KitchenOrderResponse> getActiveKitchenOrders() {
        List<OrderStatus> activeStatuses = Arrays.asList(
                OrderStatus.SENT_TO_KITCHEN,
                OrderStatus.PREPARING,
                OrderStatus.READY
        );

        List<OrderEntity> orders = orderRepository.findByStatusIn(activeStatuses);

        return orders.stream()
                .map(this::mapToKitchenOrderResponse)
                .toList();
    }

    public KitchenOrderResponse updateOrderStatus(Long orderId, KitchenOrderStatusUpdateRequest request) {
        if (request == null || request.getStatus() == null) {
            throw new InvalidKitchenStatusException("Status is required");
        }

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        validateStatusUpdate(order.getStatus(), request.getStatus());

        order.setStatus(request.getStatus());
        order.setUpdatedAt(LocalDateTime.now());

        OrderEntity savedOrder = orderRepository.save(order);

        return mapToKitchenOrderResponse(savedOrder);
    }

    private void validateStatusUpdate(OrderStatus currentStatus, OrderStatus newStatus) {
        // Only allow kitchen-related statuses
        if (!isKitchenStatus(newStatus)) {
            throw new InvalidKitchenStatusException("Invalid status for kitchen update: " + newStatus);
        }

        // Validate allowed transitions
        switch (currentStatus) {
            case SENT_TO_KITCHEN:
                if (newStatus != OrderStatus.PREPARING) {
                    throw new InvalidKitchenStatusException("SENT_TO_KITCHEN can only transition to PREPARING");
                }
                break;
            case PREPARING:
                if (newStatus != OrderStatus.READY) {
                    throw new InvalidKitchenStatusException("PREPARING can only transition to READY");
                }
                break;
            case READY:
                throw new InvalidKitchenStatusException("READY orders cannot be updated further");
            default:
                throw new InvalidKitchenStatusException("Order is not in a kitchen status: " + currentStatus);
        }
    }

    private boolean isKitchenStatus(OrderStatus status) {
        return status == OrderStatus.SENT_TO_KITCHEN ||
               status == OrderStatus.PREPARING ||
               status == OrderStatus.READY;
    }

    private KitchenOrderResponse mapToKitchenOrderResponse(OrderEntity order) {
        String tableNumber = null;
        if (order.getOrderType() == OrderType.DINE_IN) {
            tableNumber = order.getTableNumber();
        }

        List<KitchenOrderItemResponse> items = order.getItems().stream()
                .map(this::mapToKitchenOrderItemResponse)
                .toList();

        return KitchenOrderResponse.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .tableNumber(tableNumber)
                .orderType(order.getOrderType())
                .orderStatus(order.getStatus())
                .createdAt(order.getCreatedAt())
                .notes(order.getNote())
                .items(items)
                .build();
    }

    private KitchenOrderItemResponse mapToKitchenOrderItemResponse(OrderItemEntity item) {
        return KitchenOrderItemResponse.builder()
                .itemName(item.getItemName())
                .quantity(item.getQuantity())
                .notes(item.getNotes())
                .build();
    }
}