package com.bos.config.payment.dto;

import com.bos.config.common.enums.PaymentMode;
import com.bos.config.common.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long paymentId;
    private Long invoiceId;
    private PaymentMode paymentMode;
    private PaymentStatus paymentStatus;
    private BigDecimal amount;
    private String transactionRef;
    private LocalDateTime paidAt;
}