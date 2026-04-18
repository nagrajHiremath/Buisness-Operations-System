package com.bos.config.reporting.repository;

import com.bos.config.billing.entity.InvoiceEntity;
import com.bos.config.billing.repository.InvoiceRepository;
import com.bos.config.common.enums.InvoiceStatus;
import com.bos.config.common.enums.OrderStatus;
import com.bos.config.common.enums.PaymentMode;
import com.bos.config.common.enums.PaymentStatus;
import com.bos.config.order.entity.OrderEntity;
import com.bos.config.order.entity.OrderItemEntity;
import com.bos.config.order.repository.OrderRepository;
import com.bos.config.payment.entity.PaymentEntity;
import com.bos.config.payment.repository.PaymentRepository;
import com.bos.config.reporting.dto.DailySummaryResponse;
import com.bos.config.reporting.dto.OrdersSummaryResponse;
import com.bos.config.reporting.dto.TopItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReportingRepositoryImpl implements ReportingRepository {

    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public DailySummaryResponse getDailySummary(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        // Get all orders for the day
        List<OrderEntity> orders = orderRepository.findAll().stream()
                .filter(order -> order.getCreatedAt() != null &&
                        order.getCreatedAt().isAfter(startOfDay) &&
                        order.getCreatedAt().isBefore(endOfDay))
                .collect(Collectors.toList());

        Long totalOrders = (long) orders.size();

        // Get paid invoices for the day
        List<InvoiceEntity> paidInvoices = invoiceRepository.findAll().stream()
                .filter(invoice -> invoice.getStatus() == InvoiceStatus.PAID &&
                        invoice.getUpdatedAt() != null &&
                        invoice.getUpdatedAt().isAfter(startOfDay) &&
                        invoice.getUpdatedAt().isBefore(endOfDay))
                .collect(Collectors.toList());

        BigDecimal totalRevenue = paidInvoices.stream()
                .map(InvoiceEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long totalPaidInvoices = (long) paidInvoices.size();

        // Get pending payments (generated but not paid invoices)
        List<InvoiceEntity> pendingInvoices = invoiceRepository.findAll().stream()
                .filter(invoice -> invoice.getStatus() == InvoiceStatus.GENERATED)
                .collect(Collectors.toList());

        Long pendingPayments = (long) pendingInvoices.size();

        // Get successful payments for the day and group by payment mode
        List<PaymentEntity> successfulPayments = paymentRepository.findAll().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.SUCCESS &&
                        payment.getPaidAt() != null &&
                        payment.getPaidAt().isAfter(startOfDay) &&
                        payment.getPaidAt().isBefore(endOfDay))
                .collect(Collectors.toList());

        Map<PaymentMode, BigDecimal> paymentCollections = successfulPayments.stream()
                .collect(Collectors.groupingBy(
                        PaymentEntity::getPaymentMode,
                        Collectors.reducing(BigDecimal.ZERO, PaymentEntity::getAmount, BigDecimal::add)
                ));

        BigDecimal cashCollection = paymentCollections.getOrDefault(PaymentMode.CASH, BigDecimal.ZERO);
        BigDecimal upiCollection = paymentCollections.getOrDefault(PaymentMode.UPI, BigDecimal.ZERO);
        BigDecimal cardCollection = paymentCollections.getOrDefault(PaymentMode.CARD, BigDecimal.ZERO);

        return DailySummaryResponse.builder()
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .totalPaidInvoices(totalPaidInvoices)
                .pendingPayments(pendingPayments)
                .cashCollection(cashCollection)
                .upiCollection(upiCollection)
                .cardCollection(cardCollection)
                .build();
    }

    @Override
    public OrdersSummaryResponse getOrdersSummary(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<OrderEntity> orders = orderRepository.findAll().stream()
                .filter(order -> order.getCreatedAt() != null &&
                        order.getCreatedAt().isAfter(startOfDay) &&
                        order.getCreatedAt().isBefore(endOfDay))
                .collect(Collectors.toList());

        Long totalOrders = (long) orders.size();

        Map<OrderStatus, Long> orderCounts = orders.stream()
                .collect(Collectors.groupingBy(OrderEntity::getStatus, Collectors.counting()));

        Long preparingOrders = orderCounts.getOrDefault(OrderStatus.PREPARING, 0L);
        Long readyOrders = orderCounts.getOrDefault(OrderStatus.READY, 0L);
        Long cancelledOrders = orderCounts.getOrDefault(OrderStatus.CANCELLED, 0L);
        Long completedOrders = orderCounts.getOrDefault(OrderStatus.PAID, 0L);

        return OrdersSummaryResponse.builder()
                .totalOrders(totalOrders)
                .preparingOrders(preparingOrders)
                .readyOrders(readyOrders)
                .cancelledOrders(cancelledOrders)
                .completedOrders(completedOrders)
                .build();
    }

    @Override
    public List<TopItemResponse> getTopItems(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        // Get all order items for orders created today
        List<OrderItemEntity> orderItems = orderRepository.findAll().stream()
                .filter(order -> order.getCreatedAt() != null &&
                        order.getCreatedAt().isAfter(startOfDay) &&
                        order.getCreatedAt().isBefore(endOfDay))
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.toList());

        // Group by item name and aggregate
        Map<String, List<OrderItemEntity>> groupedItems = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemEntity::getItemName));

        return groupedItems.entrySet().stream()
                .map(entry -> {
                    String itemName = entry.getKey();
                    List<OrderItemEntity> items = entry.getValue();

                    Long totalQuantity = items.stream()
                            .mapToLong(item -> item.getQuantity() != null ? item.getQuantity() : 0)
                            .sum();

                    BigDecimal totalRevenue = items.stream()
                            .map(item -> item.getLineTotal() != null ? item.getLineTotal() : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return TopItemResponse.builder()
                            .itemName(itemName)
                            .totalQuantitySold(totalQuantity)
                            .totalRevenueGenerated(totalRevenue)
                            .build();
                })
                .sorted((a, b) -> Long.compare(b.getTotalQuantitySold(), a.getTotalQuantitySold()))
                .collect(Collectors.toList());
    }
}