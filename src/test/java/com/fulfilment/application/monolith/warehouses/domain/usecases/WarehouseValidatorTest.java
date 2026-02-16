package com.fulfilment.application.monolith.warehouses.domain;

import com.fulfilment.application.monolith.exception.InvalidInputException;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@QuarkusTest
public class WarehouseValidatorTest {

    @Inject
    WarehouseValidator warehouseValidator;

    @InjectMock
    WarehouseStore warehouseStore;

    @InjectMock
    LocationResolver locationResolver;

    @Test
    void shouldFailWhenWarehouseAlreadyExists() {
        Warehouse warehouse = new Warehouse("BU-001", "LOC-1", 10, 5, null, null);
        DbWarehouse existing = new DbWarehouse();

        when(warehouseStore.findActiveEntityByBusinessUnitCode("BU-001"))
                .thenReturn(existing);

        assertThrows(InvalidInputException.class,
                () -> warehouseValidator.validateWarehouse(warehouse));
    }

    @Test
    void shouldFailWhenCapacityExceedsLocationCapacity() {
        Warehouse warehouse = new Warehouse("BU-002", "LOC-1", 200, 50, null, null);

        when(warehouseStore.findActiveEntityByBusinessUnitCode("BU-002"))
                .thenReturn(null);

        Location location = new Location("LOC-1", 5, 100);
        when(locationResolver.resolveByIdentifier("LOC-1"))
                .thenReturn(location);

        when(warehouseStore.getActiveWarehouseListByLocation("LOC-1"))
                .thenReturn(List.of());

        assertThrows(InvalidInputException.class,
                () -> warehouseValidator.validateWarehouse(warehouse));
    }

    @Test
    void shouldFailWhenStockGreaterThanCapacity() {
        Warehouse warehouse = new Warehouse("BU-003", "LOC-1", 50, 80, null, null);

        when(warehouseStore.findActiveEntityByBusinessUnitCode("BU-003"))
                .thenReturn(null);

        Location location = new Location("LOC-1", 5, 100);
        when(locationResolver.resolveByIdentifier("LOC-1"))
                .thenReturn(location);

        when(warehouseStore.getActiveWarehouseListByLocation("LOC-1"))
                .thenReturn(List.of());

        assertThrows(InvalidInputException.class,
                () -> warehouseValidator.validateWarehouse(warehouse));
    }

    @Test
    void shouldFailWhenWarehouseLimitExceededInLocation() {
        Warehouse warehouse = new Warehouse("BU-004", "LOC-1", 50, 20, null, null);

        when(warehouseStore.findActiveEntityByBusinessUnitCode("BU-004"))
                .thenReturn(null);

        Location location = new Location("LOC-1", 1, 100);
        when(locationResolver.resolveByIdentifier("LOC-1"))
                .thenReturn(location);

        DbWarehouse db1 = new DbWarehouse();
        when(warehouseStore.getActiveWarehouseListByLocation("LOC-1"))
                .thenReturn(List.of(db1));

        assertThrows(InvalidInputException.class,
                () -> warehouseValidator.validateWarehouse(warehouse));
    }

    @Test
    void shouldPassValidationWhenAllRulesSatisfied() {
        Warehouse warehouse = new Warehouse("BU-005", "LOC-1", 80, 40, null, null);

        when(warehouseStore.findActiveEntityByBusinessUnitCode("BU-005"))
                .thenReturn(null);

        Location location = new Location("LOC-1", 5, 100);
        when(locationResolver.resolveByIdentifier("LOC-1"))
                .thenReturn(location);

        when(warehouseStore.getActiveWarehouseListByLocation("LOC-1"))
                .thenReturn(List.of());

        DbWarehouse result = warehouseValidator.validateWarehouse(warehouse);

        assertNull(result);
    }

    @Test
    void shouldFailArchiveValidationWhenWarehouseNotFound() {
        when(warehouseStore.findActiveEntityByBusinessUnitCode("BU-999"))
                .thenReturn(null);

        assertThrows(RuntimeException.class,
                () -> warehouseValidator.validateArchive("BU-999"));
    }

    @Test
    void shouldReturnWarehouseWhenArchiveValidationPasses() {
        DbWarehouse dbWarehouse = new DbWarehouse();
        dbWarehouse.archivedAt = null;

        when(warehouseStore.findActiveEntityByBusinessUnitCode("BU-100"))
                .thenReturn(dbWarehouse);

        DbWarehouse result = warehouseValidator.validateArchive("BU-100");

        assertNull(result.archivedAt);
    }
}
