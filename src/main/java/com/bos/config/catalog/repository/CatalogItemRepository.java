package com.bos.config.catalog.repository;

import com.bos.config.catalog.entity.CatalogItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogItemRepository extends JpaRepository<CatalogItemEntity,Long> {
}
