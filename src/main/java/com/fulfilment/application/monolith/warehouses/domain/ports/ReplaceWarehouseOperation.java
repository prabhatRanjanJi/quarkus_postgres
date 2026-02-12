package com.fulfilment.application.monolith.warehouses.domain.ports;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

public interface ReplaceWarehouseOperation {
  DbWarehouse replace(Warehouse warehouse);
}
