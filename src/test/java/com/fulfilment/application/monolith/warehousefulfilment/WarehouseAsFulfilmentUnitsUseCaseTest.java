package com.fulfilment.application.monolith.warehousefulfilment;

import com.fulfilment.application.monolith.warehousefulfilment.domain.model.WarehouseFulfilment;
import com.fulfilment.application.monolith.warehousefulfilment.services.WarehouseFulfilmentValidator;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@DisplayName("WarehouseAsFulfilmentUnitsUseCaseTest")
public class WarehouseAsFulfilmentUnitsUseCaseTest {

    @Mock
    WarehouseFulfilmentValidator warehouseFulfilmentValidator;

    @Test
    void shouldFailWhenWarehousePerProductPerStoreExceeds() {
        WarehouseFulfilment wf1 = new WarehouseFulfilment("NOIDA_Sector_62", "Table", "Warehouse_1", LocalDateTime.now());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> warehouseFulfilmentValidator.validateWarehouseFulfilment(wf1));
        assertTrue(ex.getMessage().contains("Each `Warehouse` can store maximally 5 types of `Products`"));
    }

    @Test
    void shouldFailWhenStoreExceeds() {
        WarehouseFulfilment wf1 = new WarehouseFulfilment("NOIDA_Sector_62", "Table", "Warehouse_1", LocalDateTime.now());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> warehouseFulfilmentValidator.validateWarehouseFulfilment(wf1));
        assertTrue(ex.getMessage().contains("Each `Store` can be fulfilled by a maximum of 3 different `Warehouses`"));
    }

    @Test
    void shouldFailWhenPerProductPerStoreExceeds() {
        WarehouseFulfilment wf1 = new WarehouseFulfilment("NOIDA_Sector_62", "Table", "Warehouse_1", LocalDateTime.now());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> warehouseFulfilmentValidator.validateWarehouseFulfilment(wf1));
        assertTrue(ex.getMessage().contains("Each `Product` can be fulfilled by a maximum of 2 different `Warehouses` per `Store`"));
    }

}
