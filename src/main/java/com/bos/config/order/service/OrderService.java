package com.bos.config.order.service;

import com.bos.config.catalog.entity.CatalogItemEntity;
import com.bos.config.catalog.repository.CatalogItemRepository;
import com.bos.config.common.enums.OrderStatus;
import com.bos.config.common.enums.OrderType;
import com.bos.config.order.dto.CreateOrderItemRequest;
import com.bos.config.order.dto.CreateOrderRequest;
import com.bos.config.order.dto.OrderItemResponse;
import com.bos.config.order.dto.OrderResponse;
import com.bos.config.order.entity.OrderEntity;
import com.bos.config.order.entity.OrderItemEntity;
import com.bos.config.order.repository.OrderItemRepository;
import com.bos.config.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private ModelMapper modelMapper;
    private final CatalogItemRepository catalogItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        validateCreateOrderRequest(request);

        OrderEntity order = modelMapper.map(request, OrderEntity.class);

        order.setOrderNumber(generateOrderNumber());
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setBusinessId(1L);

        BigDecimal subtotal = BigDecimal.ZERO;
        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (CreateOrderItemRequest itemRequest : request.getItems()) {

            CatalogItemEntity catalogItem = catalogItemRepository.findById(itemRequest.getCatalogItemId())
                    .orElseThrow(() -> new RuntimeException(
                            "Catalog item not found: " + itemRequest.getCatalogItemId()));

            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setCatalogItemId(catalogItem.getId());
            orderItem.setItemName(catalogItem.getName());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(catalogItem.getPrice());
            orderItem.setNotes(itemRequest.getNotes());

            BigDecimal lineTotal = catalogItem.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            orderItem.setLineTotal(lineTotal);
            orderItem.setOrder(order);

            subtotal = subtotal.add(lineTotal);
            orderItems.add(orderItem);
        }

        order.setSubtotalAmount(subtotal);
        order.setTaxAmount(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setTotalAmount(subtotal);
        order.setItems(orderItems);

        OrderEntity savedOrder = orderRepository.save(order);

        OrderResponse response = modelMapper.map(savedOrder, OrderResponse.class);
        response.setItems(
                orderItems.stream()
                        .map(item -> modelMapper.map(item, OrderItemResponse.class))
                        .toList()
        );

        return response;
    }

    private void validateCreateOrderRequest(CreateOrderRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("At least one item is required");
        }

        if (request.getOrderType() == OrderType.DINE_IN &&
                (request.getContext().getTableNumber() == null || request.getContext().getTableNumber().isBlank())) {
            throw new RuntimeException("Table number is required for dine-in orders");
        }
    }

    private String generateOrderNumber() {
    return "ORD-" + System.currentTimeMillis();
}
}
