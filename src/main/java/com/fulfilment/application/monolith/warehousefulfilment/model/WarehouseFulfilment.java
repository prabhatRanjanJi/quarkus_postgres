package com.fulfilment.application.monolith.warehousefulfilment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class WarehouseFulfilment {

    private String store;
    private String product;
    private String warehouse;
    private LocalDateTime createdAt;

    public WarehouseFulfilment(String store, String product, String warehouse, LocalDateTime createdAt) {
        this.store = store;
        this.product = product;
        this.warehouse = warehouse;
        this.createdAt = createdAt;
    }
}
