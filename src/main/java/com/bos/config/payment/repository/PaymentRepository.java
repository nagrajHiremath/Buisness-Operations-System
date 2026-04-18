package com.bos.config.payment.repository;

import com.bos.config.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByInvoiceId(Long invoiceId);

    boolean existsByInvoiceId(Long invoiceId);
}