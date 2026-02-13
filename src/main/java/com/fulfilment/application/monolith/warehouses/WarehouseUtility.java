package com.fulfilment.application.monolith.warehouses;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WarehouseUtility {

    public com.fulfilment.application.monolith.warehouses.domain.models.Warehouse convertWarehouseObject(Warehouse data) {
        com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse = new
                com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
        warehouse.businessUnitCode = data.getBusinessUnitCode();
        warehouse.location = data.getLocation();
        warehouse.capacity = data.getCapacity();
        warehouse.stock = data.getStock();
        warehouse.archivedAt = null;

        return warehouse;
    }

    public Warehouse prepareDTOFromEntity(DbWarehouse dbWarehouse) {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(String.valueOf(dbWarehouse.id));
        warehouse.setBusinessUnitCode(dbWarehouse.businessUnitCode);
        warehouse.setLocation(dbWarehouse.location);
        warehouse.setCapacity(dbWarehouse.capacity);
        warehouse.setStock(dbWarehouse.stock);

        return warehouse;
    }
}
