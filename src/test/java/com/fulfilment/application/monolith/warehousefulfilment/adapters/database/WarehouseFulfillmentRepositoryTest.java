package com.fulfilment.application.monolith.warehousefulfilment.adapters.database;

import com.fulfilment.application.monolith.warehousefulfilment.adapters.databse.WarehouseFulfilmentRepository;
import com.fulfilment.application.monolith.warehousefulfilment.domain.model.WarehouseFulfilment;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class WarehouseFulfillmentRepositoryTest {

    @Inject
    WarehouseFulfilmentRepository repository;

    @BeforeEach
    @Transactional
    void cleanDb() {
        repository.deleteAll();
    }

    @Test
    @Transactional
    void shouldReturnZeroWhenNoProductLimitReached() {
        WarehouseFulfilment fulfilment = new WarehouseFulfilment();
        fulfilment.setProduct("P1");
        fulfilment.setStore("S1");
        fulfilment.setWarehouse("W1");

        Long result = repository.validateProduct(fulfilment);

        assertEquals(0L, result);
    }

    @Test
    @Transactional
    void shouldReturnZeroWhenNoStoreLimitReached() {
        WarehouseFulfilment fulfilment = new WarehouseFulfilment();
        fulfilment.setStore("S2");
        fulfilment.setWarehouse("W1");
        fulfilment.setProduct("P1");

        Long result = repository.validateStore(fulfilment);

        assertEquals(0L, result);
    }

    @Test
    @Transactional
    void shouldReturnZeroWhenNoWarehouseLimitReached() {
        WarehouseFulfilment fulfilment = new WarehouseFulfilment();
        fulfilment.setWarehouse("W2");
        fulfilment.setStore("S1");
        fulfilment.setProduct("P1");

        Long result = repository.validateWarehouse(fulfilment);

        assertEquals(0L, result);
    }

    @Test
    @Transactional
    void shouldPersistWarehouseFulfilmentSuccessfully() {
        WarehouseFulfilment fulfilment = new WarehouseFulfilment();
        fulfilment.setWarehouse("W3");
        fulfilment.setStore("S3");
        fulfilment.setProduct("P3");

        repository.saveWarehouseFulfilment(fulfilment);

        WarehouseFulfilment probe = new WarehouseFulfilment();
        probe.setWarehouse("W3");
        probe.setStore("S3");
        probe.setProduct("P3");

        Long count = repository.validateProduct(probe);

        assertEquals(0L, count);
    }
}
