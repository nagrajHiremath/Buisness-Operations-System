package com.bos.config.order.repository;

import com.bos.config.common.enums.OrderStatus;
import com.bos.config.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByStatusIn(List<OrderStatus> statuses);
}
