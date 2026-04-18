package com.bos.config.billing.repository;

import com.bos.config.billing.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    Optional<InvoiceEntity> findByOrderId(Long orderId);
    boolean existsByOrderId(Long orderId);
}