package com.fulfilment.application.monolith.warehousefulfilment.interfaces;

import com.fulfilment.application.monolith.warehousefulfilment.model.WarehouseFulfilment;

public interface WarehouseFulfilmentStore {

    Long validateProduct(WarehouseFulfilment fulfilment);

    Long validateStore(WarehouseFulfilment fulfilment);

    Long validateWarehouse(WarehouseFulfilment fulfilment);

    void saveWarehouseFulfilment(WarehouseFulfilment fulfilment);

}
