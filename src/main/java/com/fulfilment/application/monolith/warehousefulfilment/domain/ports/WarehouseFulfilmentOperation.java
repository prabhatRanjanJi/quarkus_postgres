package com.fulfilment.application.monolith.warehousefulfilment.domain.ports;

import com.fulfilment.application.monolith.warehousefulfilment.domain.model.WarehouseFulfilment;

public interface WarehouseFulfilmentOperation {

    void save(WarehouseFulfilment warehouseFulfilment);
}
