package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ArchiveWarehouseUseCaseTest")
public class ArchiveWarehouseUseCaseTest {

    @Mock
    private WarehouseStore warehouseStore;

    @Mock
    private LocationResolver locationResolver;

    @Mock
    private ArchiveWarehouseUseCase archiveWarehouseUseCase;

    @Test
    public void shouldFailWhenBusinessUnitDoesNotExists() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);

        when(warehouseStore.findByBusinessUnitCode("MHW.0009")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> archiveWarehouseUseCase.archive(warehouse));

        assertTrue(ex.getMessage().contains("does not Exist"));
        Mockito.verify(warehouseStore, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void testWarehouseArchiveSuccess() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);
        List<Warehouse> existingWarehouses = new ArrayList<>();
        Warehouse activeWarehouse =
                new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);
        Warehouse archivedWarehouse =
                new Warehouse("MHW.000", "ZWOLLE-001", 8, 5, null, LocalDateTime.now());

        existingWarehouses.add(activeWarehouse);
        existingWarehouses.add(archivedWarehouse);

        when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(existingWarehouses.get(0));

        archiveWarehouseUseCase.archive(activeWarehouse);
        verify(warehouseStore).update(activeWarehouse);
    }
}
