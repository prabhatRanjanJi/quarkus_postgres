package com.fulfilment.application.monolith.warehousefulfilment.implementation;

import com.fulfilment.application.monolith.warehousefulfilment.interfaces.WarehouseFulfilmentOperation;
import com.fulfilment.application.monolith.warehousefulfilment.interfaces.WarehouseFulfilmentStore;
import com.fulfilment.application.monolith.warehousefulfilment.model.WarehouseFulfilment;
import com.fulfilment.application.monolith.warehousefulfilment.services.WarehouseFulfilmentValidator;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class WarehouseFulfilmentUseCase implements WarehouseFulfilmentOperation {

    private final WarehouseFulfilmentValidator warehouseFulfilmentValidator;
    private final WarehouseFulfilmentStore warehouseFulfilmentStore;

    @Override
    public void save(WarehouseFulfilment fulfilment) {
        warehouseFulfilmentValidator.validateWarehouseFulfilment(fulfilment);
        warehouseFulfilmentStore.saveWarehouseFulfilment(fulfilment);
    }
}
