package com.bos.config.billing.controller;

import com.bos.config.billing.dto.InvoiceResponse;
import com.bos.config.billing.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping("/orders/{orderId}")
    public ResponseEntity<InvoiceResponse> generateInvoice(@PathVariable Long orderId) {
        InvoiceResponse invoice = invoiceService.generateInvoice(orderId);
        return ResponseEntity.ok(invoice);
    }
}