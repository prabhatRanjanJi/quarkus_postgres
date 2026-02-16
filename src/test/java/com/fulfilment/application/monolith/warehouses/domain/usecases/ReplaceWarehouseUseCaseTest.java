package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.domain.WarehouseValidator;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class ReplaceWarehouseUseCaseTest {

    @Inject
    ReplaceWarehouseUseCase replaceWarehouseUseCase;

    @InjectMock
    WarehouseStore warehouseStore;

    @InjectMock
    WarehouseValidator warehouseValidator;

    @Test
    void shouldFailWhenBusinessUnitDoesNotExists() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);

        doThrow(new RuntimeException("does not Exists to replace"))
                .when(warehouseValidator).validateWarehouseForReplacement(warehouse);

        assertThrows(RuntimeException.class,
                () -> replaceWarehouseUseCase.replace(warehouse));
    }

    @Test
    void shouldFailWhenWarehouseCountExceedsLocationLimit() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 80, 10, null, null);

        doThrow(new RuntimeException("is already reached"))
                .when(warehouseValidator).validateWarehouseForReplacement(warehouse);

        assertThrows(RuntimeException.class,
                () -> replaceWarehouseUseCase.replace(warehouse));
    }

    @Test
    void shouldFailWhenWarehouseCapacityExceedsLocationCapacity() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 100, 60, null, null);

        doThrow(new RuntimeException("cannot exceed"))
                .when(warehouseValidator).validateWarehouseForReplacement(warehouse);

        assertThrows(RuntimeException.class,
                () -> replaceWarehouseUseCase.replace(warehouse));
    }

    @Test
    void shouldFailWhenWarehouseStockExceedsCapacity() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 50, 60, null, null);

        doThrow(new RuntimeException("cannot be more than"))
                .when(warehouseValidator).validateWarehouseForReplacement(warehouse);

        assertThrows(RuntimeException.class,
                () -> replaceWarehouseUseCase.replace(warehouse));
    }

    @Test
    void shouldFailWhenReplacingWarehouseCapacityLessThanWarehouseStock() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 30, 20, null, null);

        doThrow(new RuntimeException("cannot accomodate stock from"))
                .when(warehouseValidator).validateWarehouseForReplacement(warehouse);

        assertThrows(RuntimeException.class,
                () -> replaceWarehouseUseCase.replace(warehouse));
    }

    @Test
    void shouldFailWhenReplacingWarehouseStockNotSameAsWarehouseStock() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 100, 50, null, null);

        doThrow(new RuntimeException("being replaced is not same"))
                .when(warehouseValidator).validateWarehouseForReplacement(warehouse);

        assertThrows(RuntimeException.class,
                () -> replaceWarehouseUseCase.replace(warehouse));
    }

    @Test
    void shouldReplaceWarehouseSuccessfully() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 90, 60, null, null);
        DbWarehouse current = new DbWarehouse();

        when(warehouseValidator.validateWarehouseForReplacement(warehouse))
                .thenReturn(current);
        when(warehouseStore.create(warehouse)).thenReturn(current);

        replaceWarehouseUseCase.replace(warehouse);

        verify(warehouseValidator).validateWarehouseForReplacement(warehouse);
        verify(warehouseStore).archive(current);
        verify(warehouseStore).create(warehouse);
    }
}
