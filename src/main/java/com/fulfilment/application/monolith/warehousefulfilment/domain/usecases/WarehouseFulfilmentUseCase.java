package com.fulfilment.application.monolith.warehousefulfilment.domain.usecases;

import com.fulfilment.application.monolith.warehousefulfilment.domain.ports.WarehouseFulfilmentOperation;
import com.fulfilment.application.monolith.warehousefulfilment.domain.ports.WarehouseFulfilmentStore;
import com.fulfilment.application.monolith.warehousefulfilment.domain.model.WarehouseFulfilment;
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
