package com.fulfilment.application.monolith.warehousefulfilment.domain.ports;

import com.fulfilment.application.monolith.warehousefulfilment.domain.model.WarehouseFulfilment;

public interface WarehouseFulfilmentStore {

    Long validateProduct(WarehouseFulfilment fulfilment);

    Long validateStore(WarehouseFulfilment fulfilment);

    Long validateWarehouse(WarehouseFulfilment fulfilment);

    void saveWarehouseFulfilment(WarehouseFulfilment fulfilment);

}
