package com.bos.config.catalog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "catalog_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // multi-tenant support
    @Column(name = "business_id", nullable = false)
    private Long businessId;

    // category reference
    @Column(name = "category_id")
    private Long categoryId;

    // basic details
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    // SKU / unique code (useful later for retail/medical)
    @Column(name = "sku")
    private String sku;

    // unit like plate, piece, bottle, tablet
    @Column(name = "unit")
    private String unit;

    // pricing
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "tax_percentage", precision = 5, scale = 2)
    private BigDecimal taxPercentage;

    // availability
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "available", nullable = false)
    private Boolean available;

    // audit
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
