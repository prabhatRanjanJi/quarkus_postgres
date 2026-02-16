package com.fulfilment.application.monolith.stores;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@QuarkusTest
public class LegacyStoreManagerGatewayTest {
    @Test
    void testCreateStoreOnLegacySystem() {
        LegacyStoreManagerGateway gateway = new LegacyStoreManagerGateway();

        Store store = new Store();
        store.name = "TestStore";
        store.quantityProductsInStock = 10;

        assertDoesNotThrow(() ->
                gateway.createStoreOnLegacySystem(store)
        );
    }

    @Test
    void testUpdateStoreOnLegacySystem() {
        LegacyStoreManagerGateway gateway = new LegacyStoreManagerGateway();

        Store store = new Store();
        store.name = "UpdatedStore";
        store.quantityProductsInStock = 20;

        assertDoesNotThrow(() ->
                gateway.updateStoreOnLegacySystem(store)
        );
    }
}
