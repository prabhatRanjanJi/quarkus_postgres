package com.fulfilment.application.monolith.warehousefulfilment;

import com.fulfilment.application.monolith.warehouses.WarehouseUtility;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
class WarehouseUtilityTest {

    @Inject
    WarehouseUtility warehouseUtility;

    @Test
    void shouldConvertApiWarehouseToDomainWarehouse() {
        com.warehouse.api.beans.Warehouse apiWarehouse = new com.warehouse.api.beans.Warehouse();
        apiWarehouse.setBusinessUnitCode("BU-001");
        apiWarehouse.setLocation("LOC-1");
        apiWarehouse.setCapacity(100);
        apiWarehouse.setStock(40);

        com.fulfilment.application.monolith.warehouses.domain.models.Warehouse result =
                warehouseUtility.convertWarehouseObject(apiWarehouse);

        assertEquals("BU-001", result.businessUnitCode);
        assertEquals("LOC-1", result.location);
        assertEquals(100, result.capacity);
        assertEquals(40, result.stock);
        assertNull(result.archivedAt);
    }

    @Test
    void shouldPrepareDTOFromDbEntity() {
        DbWarehouse dbWarehouse = new DbWarehouse();
        dbWarehouse.id = 10L;
        dbWarehouse.businessUnitCode = "BU-002";
        dbWarehouse.location = "LOC-2";
        dbWarehouse.capacity = 200;
        dbWarehouse.stock = 80;

        com.warehouse.api.beans.Warehouse dto =
                warehouseUtility.prepareDTOFromEntity(dbWarehouse);

        assertEquals("10", dto.getId());
        assertEquals("BU-002", dto.getBusinessUnitCode());
        assertEquals("LOC-2", dto.getLocation());
        assertEquals(200, dto.getCapacity());
        assertEquals(80, dto.getStock());
    }
}