package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
@DisplayName("ReplaceWarehouseUseCaseTest")
public class ReplaceWarehouseUseCaseTest {

    @Mock
    private WarehouseStore warehouseStore;

    @Mock
    private LocationResolver locationResolver;

    @Mock
    private ReplaceWarehouseUseCase replaceWarehouseUseCase;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Test
    void shouldFailWhenBusinessUnitDoesNotExists() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);

        when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> replaceWarehouseUseCase.replace(warehouse));

        assertTrue(ex.getMessage().contains("does not Exists to replace"));
        verify(warehouseStore, never()).create(any());
    }

    @Test
    void shouldFailWhenWarehouseCountExceedsLocationLimit() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 80, 10, null, null);
        Warehouse existingWarehouse =
                new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);

        when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(existingWarehouse);

        Location location = new Location("ZWOLLE-001", 0, 0);
        when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> replaceWarehouseUseCase.replace(warehouse));

        assertTrue(ex.getMessage().contains("is already reached"));
        verify(warehouseStore, never()).create(any());
    }

    @Test
    void shouldFailWhenWarehouseCapacityExceedsLocationCapacity() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 100, 60, null, null);
        Warehouse existingWarehouse =
                new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);

        when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(existingWarehouse);

        Location location = new Location("ZWOLLE-001", 5, 50);
        when(locationResolver.resolveByIdentifier("ZWOLLE-001"))
                .thenReturn(location);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> replaceWarehouseUseCase.replace(warehouse));

        assertTrue(ex.getMessage().contains("cannot exceed"));
        verify(warehouseStore, never()).create(any());
    }

    @Test
    void shouldFailWhenWarehouseStockExceedsCapacity() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 50, 60, null, null);
        Warehouse existingWarehouse =
                new Warehouse("MHW.000", "ZWOLLE-001", 90, 60, null, null);

        when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(existingWarehouse);


        Location location = new Location("ZWOLLE-001", 5, 100);
        when(locationResolver.resolveByIdentifier("ZWOLLE-001"))
                .thenReturn(location);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> replaceWarehouseUseCase.replace(warehouse));

        assertTrue(ex.getMessage().contains("cannot be more than"));
        verify(warehouseStore, never()).create(any());
    }

    @Test
    void shouldFailWhenReplacingWarehouseCapacityLessThanWarehouseStock() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 30, 20, null, null);
        Warehouse existingWarehouse =
                new Warehouse("MHW.000", "ZWOLLE-001", 50, 40, null, null);

        when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(existingWarehouse);


        Location location = new Location("ZWOLLE-001", 5, 100);
        when(locationResolver.resolveByIdentifier("ZWOLLE-001"))
                .thenReturn(location);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> replaceWarehouseUseCase.replace(warehouse));

        assertTrue(ex.getMessage().contains("cannot accomodate stock from"));
        verify(warehouseStore, never()).create(any());
    }

    @Test
    void shouldFailWhenReplacingWarehouseStockNotSameAsWarehouseStock() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 100, 50, null, null);
        Warehouse existingWarehouse =
                new Warehouse("MHW.000", "ZWOLLE-001", 50, 40, null, null);

        when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(existingWarehouse);


        Location location = new Location("ZWOLLE-001", 5, 100);
        when(locationResolver.resolveByIdentifier("ZWOLLE-001"))
                .thenReturn(location);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> replaceWarehouseUseCase.replace(warehouse));

        assertTrue(ex.getMessage().contains("being replaced is not same"));
        verify(warehouseStore, never()).create(any());
    }

    @Test
    void shouldReplaceWarehouseSuccessfully() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 90, 60, null, null);
        Warehouse existingWarehouse =
                new Warehouse("MHW.000", "ZWOLLE-001", 70, 60, null, null);

        when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(existingWarehouse);

        Location location = new Location("ZWOLLE-001", 5, 100);
        when(locationResolver.resolveByIdentifier("ZWOLLE-001"))
                .thenReturn(location);

        replaceWarehouseUseCase.replace(warehouse);

        verify(warehouseStore).update(existingWarehouse);
        verify(warehouseStore).create(warehouse);
    }
}
