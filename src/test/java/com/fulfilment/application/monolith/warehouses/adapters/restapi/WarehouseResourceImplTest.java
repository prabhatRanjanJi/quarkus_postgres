package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.WarehouseUtility;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.usecases.ArchiveWarehouseUseCase;
import com.fulfilment.application.monolith.warehouses.domain.usecases.CreateWarehouseUseCase;
import com.fulfilment.application.monolith.warehouses.domain.usecases.ReplaceWarehouseUseCase;
import com.warehouse.api.beans.Warehouse;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class WarehouseResourceImplTest {

    @Inject
    WarehouseResourceImpl resource;

    @InjectMock
    WarehouseRepository warehouseRepository;

    @InjectMock
    CreateWarehouseUseCase createWarehouseUseCase;

    @InjectMock
    ArchiveWarehouseUseCase archiveWarehouseUseCase;

    @InjectMock
    ReplaceWarehouseUseCase replaceWarehouseUseCase;

    @InjectMock
    WarehouseUtility warehouseUtility;

    @Test
    void shouldListAllWarehousesUnits() {
        var domainWarehouse = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
        domainWarehouse.businessUnitCode = "BU-1";
        domainWarehouse.location = "BLR";
        domainWarehouse.capacity = 100;
        domainWarehouse.stock = 50;

        when(warehouseRepository.getAll()).thenReturn(List.of(domainWarehouse));

        List<Warehouse> result = resource.listAllWarehousesUnits();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BU-1", result.get(0).getBusinessUnitCode());

        verify(warehouseRepository, times(1)).getAll();
    }

    @Test
    void shouldCreateANewWarehouseUnit() throws Exception {
        Warehouse request = new Warehouse();
        request.setBusinessUnitCode("BU-2");
        request.setLocation("BLR");
        request.setCapacity(200);
        request.setStock(75);

        var converted = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();

        when(warehouseUtility.convertWarehouseObject(request)).thenReturn(converted);

        Warehouse response = resource.createANewWarehouseUnit(request);

        assertNotNull(response);
        verify(warehouseUtility, times(1)).convertWarehouseObject(request);
        verify(createWarehouseUseCase, times(1)).create(converted);
    }

    @Test
    void shouldGetWarehouseById() {
        String id = "1";
        DbWarehouse dbWarehouse = new DbWarehouse();
        Warehouse dto = new Warehouse();

        when(warehouseRepository.findByWarehouseId(1L)).thenReturn(dbWarehouse);
        when(warehouseUtility.prepareDTOFromEntity(dbWarehouse)).thenReturn(dto);

        Warehouse result = resource.getAWarehouseUnitByID(id);

        assertNotNull(result);
        verify(warehouseRepository).findByWarehouseId(1L);
        verify(warehouseUtility).prepareDTOFromEntity(dbWarehouse);
    }

    @Test
    void shouldArchiveWarehouseById() {
        resource.archiveAWarehouseUnitByID("10");

        verify(archiveWarehouseUseCase, times(1)).archiveById("10");
    }

    @Test
    void shouldReplaceCurrentActiveWarehouse() {
        Warehouse request = new Warehouse();
        request.setBusinessUnitCode("BU-5");

        var domainWarehouse = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
        DbWarehouse replacedEntity = new DbWarehouse();
        Warehouse expectedResponse = new Warehouse();

        when(warehouseUtility.convertWarehouseObject(request)).thenReturn(domainWarehouse);
        when(replaceWarehouseUseCase.replace(domainWarehouse)).thenReturn(replacedEntity);
        when(warehouseUtility.prepareDTOFromEntity(replacedEntity)).thenReturn(expectedResponse);

        Warehouse result = resource.replaceTheCurrentActiveWarehouse("BU-5", request);

        assertNotNull(result);
        verify(warehouseUtility).convertWarehouseObject(request);
        verify(replaceWarehouseUseCase).replace(domainWarehouse);
        verify(warehouseUtility).prepareDTOFromEntity(replacedEntity);
    }
}
