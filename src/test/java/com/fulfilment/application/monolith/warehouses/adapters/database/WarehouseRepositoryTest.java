package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.exception.InvalidInputException;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class WarehouseRepositoryTest {

    @Inject
    WarehouseRepository warehouseRepository;

    private Warehouse createTestWarehouse(String buCode) {
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = buCode;
        warehouse.location = "BLR";
        warehouse.capacity = 100;
        warehouse.stock = 50;
        warehouse.archivedAt = null;
        return warehouse;
    }

    @Test
    @Transactional
    void shouldCreateWarehouseSuccessfully() {
        Warehouse warehouse = createTestWarehouse("BU-001");

        DbWarehouse saved = warehouseRepository.create(warehouse);

        assertNotNull(saved);
        assertEquals("BU-001", saved.businessUnitCode);
        assertEquals("BLR", saved.location);
    }

    @Test
    void shouldThrowExceptionWhenWarehouseIsNullOnCreate() {
        assertThrows(InvalidInputException.class,
                () -> warehouseRepository.create(null));
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenDuplicateBusinessUnitCodeExists() {
        Warehouse warehouse = createTestWarehouse("BU-002");
        warehouseRepository.create(warehouse);

        Warehouse duplicate = createTestWarehouse("BU-002");

        assertThrows(InvalidInputException.class,
                () -> warehouseRepository.create(duplicate));
    }

    @Test
    @Transactional
    void shouldUpdateWarehouseSuccessfully() {
        Warehouse warehouse = createTestWarehouse("BU-003");
        warehouseRepository.create(warehouse);

        warehouse.location = "HYD";
        warehouse.capacity = 200;
        warehouse.stock = 150;
        warehouse.archivedAt = null;

        assertDoesNotThrow(() -> warehouseRepository.update(warehouse));

        DbWarehouse updated =
                warehouseRepository.findActiveEntityByBusinessUnitCode("BU-003");

        assertNotNull(updated);
        assertEquals("HYD", updated.location);
        assertEquals(200, updated.capacity);
        assertEquals(150, updated.stock);

    }

    @Test
    void shouldThrowExceptionWhenUpdatingNullWarehouse() {
        assertThrows(InvalidInputException.class,
                () -> warehouseRepository.update(null));
    }

    @Test
    @Transactional
    void shouldRemoveWarehouseSuccessfully() {
        Warehouse warehouse = createTestWarehouse("BU-004");
        warehouseRepository.create(warehouse);

        assertDoesNotThrow(() -> warehouseRepository.remove(warehouse));
    }

    @Test
    void shouldThrowExceptionWhenRemovingNullWarehouse() {
        assertThrows(InvalidInputException.class,
                () -> warehouseRepository.remove(null));
    }

    @Test
    @Transactional
    void shouldFindWarehouseByBusinessUnitCode() {
        Warehouse warehouse = createTestWarehouse("BU-005");
        warehouseRepository.create(warehouse);

        Warehouse found =
                warehouseRepository.findByBusinessUnitCode("BU-005");

        assertNotNull(found);
        assertEquals("BU-005", found.businessUnitCode);
    }

    @Test
    @Transactional
    void shouldArchiveWarehouse() {
        Warehouse warehouse = createTestWarehouse("BU-006");
        DbWarehouse dbWarehouse = warehouseRepository.create(warehouse);

        warehouseRepository.archive(dbWarehouse);

        assertNotNull(dbWarehouse.archivedAt);
    }

    @Test
    @Transactional
    void shouldReturnActiveWarehouseListByLocation() {
        Warehouse warehouse = createTestWarehouse("BU-007");
        warehouse.location = "MUM";
        warehouseRepository.create(warehouse);

        List<DbWarehouse> activeList =
                warehouseRepository.getActiveWarehouseListByLocation("MUM");

        assertFalse(activeList.isEmpty());
        assertEquals("MUM", activeList.get(0).location);
    }
}
