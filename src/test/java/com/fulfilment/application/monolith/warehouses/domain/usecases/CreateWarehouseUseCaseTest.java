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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@QuarkusTest
//@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
@DisplayName("CreateWarehouseUseCase")
public class CreateWarehouseUseCaseTest {

    @Mock
    private  WarehouseStore warehouseStore;

    @Mock
    private  LocationResolver locationResolver;

    @Mock
    private  CreateWarehouseUseCase createWarehouseUseCase;

    @Mock
    private  WarehouseRepository warehouseRepository;

    @Test
    void shouldFailWhenBusinessUnitAlreadyExists() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);
        when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(warehouse);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> createWarehouseUseCase.create(warehouse));

        assertTrue(ex.getMessage().contains("already Exists"));
        verify(warehouseStore, never()).create(any());
    }

    @Test
    void shouldFailWhenLocationInvalid() {
        Warehouse warehouse = new Warehouse("MHW.000", "INVALID_LOC", 10, 10, null, null);

        when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(warehouse);
        when(locationResolver.resolveByIdentifier("INVALID_LOC")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> createWarehouseUseCase.create(warehouse));

        assertTrue(ex.getMessage().contains("not found"));
        verify(warehouseStore, never()).create(any());
    }

    @Test
    void shouldFailWhenWarehouseCountExceedsLocationLimit() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 10, 10, null, null);

        when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(warehouse);

        Location location = new Location("ZWOLLE-001", 5, 50);
        when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> createWarehouseUseCase.create(warehouse));

        assertTrue(ex.getMessage().contains("can not be more than"));
        verify(warehouseStore, never()).create(any());
    }

    @Test
    void shouldFailWhenWarehouseCapacityExceedsLocationCapacity() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 100, 60, null, null);

        when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(warehouse);

        Location location = new Location("ZWOLLE-001", 5, 50);
        when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> createWarehouseUseCase.create(warehouse));

        assertTrue(ex.getMessage().contains("can not be more than"));
        verify(warehouseStore, never()).create(any());
    }

    @Test
    void shouldFailWhenWarehouseStockExceedsCapacity() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 50, 60, null, null);

        when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(warehouse);

        Location location = new Location("ZWOLLE-001", 5, 100);

        when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> createWarehouseUseCase.create(warehouse));

        assertTrue(ex.getMessage().contains("cannot be more than"));

        verify(warehouseStore, never()).create(any());
    }

    @Test
    void shouldCreateWarehouseSuccessfully() {
        Warehouse warehouse = new Warehouse("MHW.000", "ZWOLLE-001", 90, 60, null, null);

        when(warehouseStore.findByBusinessUnitCode("MHW.000")).thenReturn(warehouse);

        Location location = new Location("ZWOLLE-001", 5, 100);

        when(locationResolver.resolveByIdentifier("ZWOLLE-001")).thenReturn(location);

        verify(warehouseStore).create(warehouse);
    }
}
