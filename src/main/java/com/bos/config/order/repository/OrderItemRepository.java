package com.bos.config.order.repository;

import com.bos.config.order.entity.OrderEntity;
import com.bos.config.order.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}
