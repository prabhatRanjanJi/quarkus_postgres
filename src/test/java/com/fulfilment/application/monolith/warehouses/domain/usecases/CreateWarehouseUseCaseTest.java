package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.WarehouseValidator;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@QuarkusTest
public class CreateWarehouseUseCaseTest {

    @Inject
    CreateWarehouseUseCase createWarehouseUseCase;

    @InjectMock
    WarehouseStore warehouseStore;

    @InjectMock
    WarehouseValidator warehouseValidator;

    @Test
    void shouldFailWhenBusinessUnitAlreadyExists() throws Exception {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);

        doThrow(new RuntimeException("already Exists"))
                .when(warehouseValidator).validateWarehouse(warehouse);

        assertThrows(RuntimeException.class,
                () -> createWarehouseUseCase.create(warehouse));

        verify(warehouseStore, never()).create(any());
    }

    @Test
    void shouldFailWhenLocationInvalid() throws Exception {
        Warehouse warehouse = new Warehouse("MHW.000", "INVALID_LOC", 10, 10, null, null);

        doThrow(new RuntimeException("Location not found"))
                .when(warehouseValidator).validateWarehouse(warehouse);

        assertThrows(RuntimeException.class,
                () -> createWarehouseUseCase.create(warehouse));

        verify(warehouseStore, never()).create(any());
    }

    @Test
    void shouldFailWhenWarehouseCountExceedsLocationLimit() throws Exception {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);

        doThrow(new RuntimeException("can not be more than"))
                .when(warehouseValidator).validateWarehouse(warehouse);

        assertThrows(RuntimeException.class,
                () -> createWarehouseUseCase.create(warehouse));

        verify(warehouseStore, never()).create(any());
    }

    @Test
    void shouldFailWhenWarehouseCapacityExceedsLocationCapacity() throws Exception {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 100, 60, null, null);

        doThrow(new RuntimeException("can not be more than"))
                .when(warehouseValidator).validateWarehouse(warehouse);

        assertThrows(RuntimeException.class,
                () -> createWarehouseUseCase.create(warehouse));

        verify(warehouseStore, never()).create(any());
    }

    @Test
    void shouldFailWhenWarehouseStockExceedsCapacity() throws Exception {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 50, 60, null, null);

        doThrow(new RuntimeException("cannot be more than"))
                .when(warehouseValidator).validateWarehouse(warehouse);

        assertThrows(RuntimeException.class,
                () -> createWarehouseUseCase.create(warehouse));

        verify(warehouseStore, never()).create(any());
    }

    @Test
    void shouldCreateWarehouseSuccessfully() throws Exception {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 90, 60, null, null);

        createWarehouseUseCase.create(warehouse);

        verify(warehouseValidator).validateWarehouse(warehouse);
        verify(warehouseStore).create(warehouse);
    }
}
