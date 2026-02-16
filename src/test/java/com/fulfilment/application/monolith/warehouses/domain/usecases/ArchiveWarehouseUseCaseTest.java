package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.domain.WarehouseValidator;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@QuarkusTest
class ArchiveWarehouseUseCaseTest {

    @Inject
    ArchiveWarehouseUseCase archiveWarehouseUseCase;

    @InjectMock
    WarehouseStore warehouseStore;

    @InjectMock
    WarehouseValidator warehouseValidator;

    @Test
    void archive_shouldArchiveWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "BU-001";

        DbWarehouse dbWarehouse = new DbWarehouse();
        dbWarehouse.businessUnitCode = "BU-001";

        when(warehouseValidator.validateArchive("BU-001")).thenReturn(dbWarehouse);

        assertDoesNotThrow(() -> archiveWarehouseUseCase.archive(warehouse));

        verify(warehouseValidator, times(1)).validateArchive("BU-001");
        verify(warehouseStore, times(1)).archive(dbWarehouse);
    }

    @Test
    void archiveById_shouldArchiveUsingId() {
        String id = "5";

        DbWarehouse dbWarehouse = new DbWarehouse();
        dbWarehouse.businessUnitCode = "BU-002";

        when(warehouseStore.findByWarehouseId(5L)).thenReturn(dbWarehouse);
        when(warehouseValidator.validateArchive("BU-002")).thenReturn(dbWarehouse);

        assertDoesNotThrow(() -> archiveWarehouseUseCase.archiveById(id));

        verify(warehouseStore, times(1)).findByWarehouseId(5L);
        verify(warehouseValidator, times(1)).validateArchive("BU-002");
        verify(warehouseStore, times(1)).archive(dbWarehouse);
    }

    @Test
    void archive_shouldThrowExceptionWhenValidationFails() {
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "INVALID";

        when(warehouseValidator.validateArchive("INVALID"))
                .thenThrow(new RuntimeException("Warehouse does not exist"));

        assertThrows(RuntimeException.class,
                () -> archiveWarehouseUseCase.archive(warehouse));

        verify(warehouseValidator, times(1)).validateArchive("INVALID");
        verify(warehouseStore, never()).archive(any());
    }
}
