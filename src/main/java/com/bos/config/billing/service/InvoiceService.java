package com.bos.config.billing.service;

import com.bos.config.billing.dto.InvoiceResponse;
import com.bos.config.billing.entity.InvoiceEntity;
import com.bos.config.billing.exceptions.InvoiceAlreadyExistsException;
import com.bos.config.billing.exceptions.InvalidOrderStatusException;
import com.bos.config.billing.exceptions.OrderNotFoundException;
import com.bos.config.billing.repository.InvoiceRepository;
import com.bos.config.common.enums.InvoiceStatus;
import com.bos.config.common.enums.OrderStatus;
import com.bos.config.order.entity.OrderEntity;
import com.bos.config.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    @Autowired
    private ModelMapper modelMapper;
    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public InvoiceResponse generateInvoice(Long orderId) {
        // Fetch order
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        // Validate order status
        validateOrderStatus(order.getStatus());

        // Check if invoice already exists
        if (invoiceRepository.existsByOrderId(orderId)) {
            throw new InvoiceAlreadyExistsException("Invoice already exists for order: " + orderId);
        }

        // Calculate amounts
        BigDecimal subtotal = order.getSubtotalAmount() != null ? order.getSubtotalAmount() : BigDecimal.ZERO;
        BigDecimal tax = order.getTaxAmount() != null ? order.getTaxAmount() : BigDecimal.ZERO;
        BigDecimal discount = order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal total = order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO;

        // Generate invoice number
        String invoiceNumber = generateInvoiceNumber();

        // Create invoice
        InvoiceEntity invoice = InvoiceEntity.builder()
                .businessId(order.getBusinessId())
                .orderId(orderId)
                .invoiceNumber(invoiceNumber)
                .status(InvoiceStatus.GENERATED)
                .subtotalAmount(subtotal)
                .taxAmount(tax)
                .discountAmount(discount)
                .totalAmount(total)
                .generatedByUserId(1L) // TODO: Get from security context
                .generatedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Save invoice
        InvoiceEntity savedInvoice = invoiceRepository.save(invoice);

        // Return response
        return InvoiceResponse.builder()
                .invoiceId(savedInvoice.getId())
                .invoiceNumber(savedInvoice.getInvoiceNumber())
                .orderId(savedInvoice.getOrderId())
                .invoiceStatus(savedInvoice.getStatus())
                .subtotalAmount(savedInvoice.getSubtotalAmount())
                .taxAmount(savedInvoice.getTaxAmount())
                .discountAmount(savedInvoice.getDiscountAmount())
                .totalAmount(savedInvoice.getTotalAmount())
                .generatedAt(savedInvoice.getGeneratedAt())
                .build();
    }

    private void validateOrderStatus(OrderStatus status) {
        if (status == OrderStatus.CANCELLED) {
            throw new InvalidOrderStatusException("Cannot generate invoice for cancelled order");
        }

        if (status != OrderStatus.SENT_TO_KITCHEN &&
            status != OrderStatus.PREPARING &&
            status != OrderStatus.READY) {
            throw new InvalidOrderStatusException("Invoice can only be generated for orders in kitchen status");
        }
    }

    private String generateInvoiceNumber() {
        return "INV-" + System.currentTimeMillis();
    }
}