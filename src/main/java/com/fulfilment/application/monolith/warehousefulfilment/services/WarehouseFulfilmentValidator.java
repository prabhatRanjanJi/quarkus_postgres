package com.fulfilment.application.monolith.warehousefulfilment.services;

import com.fulfilment.application.monolith.warehousefulfilment.domain.ports.WarehouseFulfilmentStore;
import com.fulfilment.application.monolith.warehousefulfilment.domain.model.WarehouseFulfilment;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class WarehouseFulfilmentValidator {

    private final WarehouseFulfilmentStore warehouseFulfilmentStore;

    public void validateWarehouseFulfilment(WarehouseFulfilment fulfilment) {
        if (warehouseFulfilmentStore.validateProduct(fulfilment) > Long.valueOf(0)) {
            throw new RuntimeException("Each `Product` can be fulfilled by a maximum of 2 different `Warehouses` per `Store`");
        }
        if (warehouseFulfilmentStore.validateStore(fulfilment) > Long.valueOf(0)) {
            throw new RuntimeException("Each `Store` can be fulfilled by a maximum of 3 different `Warehouses`");
        }
        if (warehouseFulfilmentStore.validateWarehouse(fulfilment) > Long.valueOf(0)) {
            throw new RuntimeException("Each `Warehouse` can store maximally 5 types of `Products`");
        }
    }
}
