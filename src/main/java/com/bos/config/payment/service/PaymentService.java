package com.bos.config.payment.service;

import com.bos.config.billing.entity.InvoiceEntity;
import com.bos.config.billing.repository.InvoiceRepository;
import com.bos.config.common.enums.InvoiceStatus;
import com.bos.config.common.enums.PaymentStatus;
import com.bos.config.payment.dto.PaymentRequest;
import com.bos.config.payment.dto.PaymentResponse;
import com.bos.config.payment.entity.PaymentEntity;
import com.bos.config.payment.exceptions.InvoiceAlreadyPaidException;
import com.bos.config.payment.exceptions.InvoiceNotFoundException;
import com.bos.config.payment.exceptions.PaymentAmountMismatchException;
import com.bos.config.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        // Validate invoice exists
        InvoiceEntity invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with id: " + request.getInvoiceId()));

        // Validate invoice status
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new InvoiceAlreadyPaidException("Invoice is already paid: " + request.getInvoiceId());
        }

        // Validate payment amount matches invoice total
        if (request.getAmount().compareTo(invoice.getTotalAmount()) != 0) {
            throw new PaymentAmountMismatchException(
                String.format("Payment amount %s does not match invoice total %s",
                    request.getAmount(), invoice.getTotalAmount()));
        }

        // Check if payment already exists for this invoice
        if (paymentRepository.existsByInvoiceId(request.getInvoiceId())) {
            throw new InvoiceAlreadyPaidException("Payment already exists for invoice: " + request.getInvoiceId());
        }

        LocalDateTime now = LocalDateTime.now();

        // Create payment entity
        PaymentEntity payment = PaymentEntity.builder()
                .invoiceId(request.getInvoiceId())
                .paymentMode(request.getPaymentMode())
                .amount(request.getAmount())
                .transactionRef(request.getTransactionRef())
                .status(PaymentStatus.SUCCESS)
                .paidAt(now)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Save payment
        PaymentEntity savedPayment = paymentRepository.save(payment);

        // Update invoice status to PAID
        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setUpdatedAt(now);
        invoiceRepository.save(invoice);

        // Return response
        return PaymentResponse.builder()
                .paymentId(savedPayment.getId())
                .invoiceId(savedPayment.getInvoiceId())
                .paymentMode(savedPayment.getPaymentMode())
                .paymentStatus(savedPayment.getStatus())
                .amount(savedPayment.getAmount())
                .transactionRef(savedPayment.getTransactionRef())
                .paidAt(savedPayment.getPaidAt())
                .build();
    }
}