package com.fulfilment.application.monolith.warehousefulfilment.domain.usecase;

import com.fulfilment.application.monolith.warehousefulfilment.domain.model.WarehouseFulfilment;
import com.fulfilment.application.monolith.warehousefulfilment.domain.ports.WarehouseFulfilmentStore;
import com.fulfilment.application.monolith.warehousefulfilment.domain.usecases.WarehouseFulfilmentUseCase;
import com.fulfilment.application.monolith.warehousefulfilment.services.WarehouseFulfilmentValidator;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@QuarkusTest
class WarehouseFulfilmentUseCaseTest {

    @Inject
    WarehouseFulfilmentUseCase useCase;

    @InjectMock
    WarehouseFulfilmentValidator warehouseFulfilmentValidator;

    @InjectMock
    WarehouseFulfilmentStore warehouseFulfilmentStore;

    @Test
    void shouldSaveWarehouseFulfilmentSuccessfully() {
        WarehouseFulfilment fulfilment = new WarehouseFulfilment();
        fulfilment.setWarehouse("W1");
        fulfilment.setStore("S1");
        fulfilment.setProduct("P1");

        doNothing().when(warehouseFulfilmentValidator)
                .validateWarehouseFulfilment(fulfilment);

        doNothing().when(warehouseFulfilmentStore)
                .saveWarehouseFulfilment(fulfilment);

        useCase.save(fulfilment);

        verify(warehouseFulfilmentValidator)
                .validateWarehouseFulfilment(fulfilment);
        verify(warehouseFulfilmentStore)
                .saveWarehouseFulfilment(fulfilment);
    }

    @Test
    void shouldNotSaveWhenValidationFails() {
        WarehouseFulfilment fulfilment = new WarehouseFulfilment();
        fulfilment.setWarehouse("W1");
        fulfilment.setStore("S1");
        fulfilment.setProduct("P1");

        doThrow(new RuntimeException("Validation failed"))
                .when(warehouseFulfilmentValidator)
                .validateWarehouseFulfilment(fulfilment);

        assertThrows(RuntimeException.class, () -> useCase.save(fulfilment));

        verify(warehouseFulfilmentValidator)
                .validateWarehouseFulfilment(fulfilment);
        verify(warehouseFulfilmentStore, never())
                .saveWarehouseFulfilment(fulfilment);
    }
}