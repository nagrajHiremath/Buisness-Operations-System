package com.bos.config.order.dto;

import com.bos.config.common.enums.OrderStatus;
import com.bos.config.common.enums.OrderType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HospitalityOrderContextResponse {

    private String tableNumber;
    private Integer guestCount;
    private String specialInstructions;
}