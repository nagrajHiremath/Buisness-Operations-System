package com.bos.config.order.dto;

import lombok.Data;

@Data
public class HospitalityOrderContextRequest {

    private String tableNumber;
    private Integer guestCount;
    private String specialInstructions;
}
