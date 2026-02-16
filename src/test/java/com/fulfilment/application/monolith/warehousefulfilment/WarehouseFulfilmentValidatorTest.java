package com.fulfilment.application.monolith.warehousefulfilment;

import com.fulfilment.application.monolith.warehousefulfilment.domain.model.WarehouseFulfilment;
import com.fulfilment.application.monolith.warehousefulfilment.domain.ports.WarehouseFulfilmentStore;
import com.fulfilment.application.monolith.warehousefulfilment.services.WarehouseFulfilmentValidator;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class WarehouseFulfilmentValidatorTest {

    @Inject
    WarehouseFulfilmentValidator validator;

    @InjectMock
    WarehouseFulfilmentStore warehouseFulfilmentStore;

    @Test
    void shouldThrowExceptionWhenProductValidationFails() {
        WarehouseFulfilment fulfilment = new WarehouseFulfilment();

        Mockito.when(warehouseFulfilmentStore.validateProduct(fulfilment))
                .thenReturn(1L);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> validator.validateWarehouseFulfilment(fulfilment)
        );

        assert exception.getMessage().contains("Product");
    }

    @Test
    void shouldThrowExceptionWhenStoreValidationFails() {
        WarehouseFulfilment fulfilment = new WarehouseFulfilment();

        Mockito.when(warehouseFulfilmentStore.validateProduct(fulfilment))
                .thenReturn(0L);
        Mockito.when(warehouseFulfilmentStore.validateStore(fulfilment))
                .thenReturn(1L);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> validator.validateWarehouseFulfilment(fulfilment)
        );

        assert exception.getMessage().contains("Store");
    }

    @Test
    void shouldThrowExceptionWhenWarehouseValidationFails() {
        WarehouseFulfilment fulfilment = new WarehouseFulfilment();

        Mockito.when(warehouseFulfilmentStore.validateProduct(fulfilment))
                .thenReturn(0L);
        Mockito.when(warehouseFulfilmentStore.validateStore(fulfilment))
                .thenReturn(0L);
        Mockito.when(warehouseFulfilmentStore.validateWarehouse(fulfilment))
                .thenReturn(1L);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> validator.validateWarehouseFulfilment(fulfilment)
        );

        assert exception.getMessage().contains("Warehouse");
    }

    @Test
    void shouldPassValidationWhenAllChecksAreZero() {
        WarehouseFulfilment fulfilment = new WarehouseFulfilment();

        Mockito.when(warehouseFulfilmentStore.validateProduct(fulfilment))
                .thenReturn(0L);
        Mockito.when(warehouseFulfilmentStore.validateStore(fulfilment))
                .thenReturn(0L);
        Mockito.when(warehouseFulfilmentStore.validateWarehouse(fulfilment))
                .thenReturn(0L);

        assertDoesNotThrow(() ->
                validator.validateWarehouseFulfilment(fulfilment)
        );
    }
}

