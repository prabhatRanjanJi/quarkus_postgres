package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class DbWarehouseTest {

    @Test
    void toWarehouse_shouldMapAllFieldsCorrectly() {
        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime archivedTime = LocalDateTime.now();

        DbWarehouse entity = new DbWarehouse();
        entity.id = 1L;
        entity.businessUnitCode = "BU-001";
        entity.location = "Bangalore";
        entity.capacity = 100;
        entity.stock = 50;
        entity.createdAt = createdTime;
        entity.archivedAt = archivedTime;

        Warehouse result = entity.toWarehouse();

        assertNotNull(result);
        assertEquals("BU-001", result.businessUnitCode);
        assertEquals("Bangalore", result.location);
        assertEquals(100, result.capacity);
        assertEquals(50, result.stock);
        assertEquals(createdTime, result.createdAt);
        assertEquals(archivedTime, result.archivedAt);
    }

    @Test
    void toWarehouse_shouldHandleNullArchivedAt() {
        DbWarehouse entity = new DbWarehouse();
        entity.businessUnitCode = "BU-002";
        entity.location = "Hyderabad";
        entity.capacity = 200;
        entity.stock = 80;
        entity.createdAt = LocalDateTime.now();
        entity.archivedAt = null;

        Warehouse result = entity.toWarehouse();

        assertNotNull(result);
        assertEquals("BU-002", result.businessUnitCode);
        assertEquals("Hyderabad", result.location);
        assertEquals(200, result.capacity);
        assertEquals(80, result.stock);
        assertNull(result.archivedAt);
    }

    @Test
    void toWarehouse_shouldWorkWithAllNullFields() {
        DbWarehouse entity = new DbWarehouse();

        Warehouse result = entity.toWarehouse();

        assertNotNull(result);
        assertNull(result.businessUnitCode);
        assertNull(result.location);
        assertNull(result.capacity);
        assertNull(result.stock);
        assertNull(result.createdAt);
        assertNull(result.archivedAt);
    }

    @Test
    void toWarehouse_shouldReturnNewObjectEachTime() {
        DbWarehouse entity = new DbWarehouse();
        entity.businessUnitCode = "BU-003";

        Warehouse first = entity.toWarehouse();
        Warehouse second = entity.toWarehouse();

        assertNotSame(first, second); // ensures no shared reference
    }
}
