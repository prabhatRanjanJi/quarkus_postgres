package com.fulfilment.application.monolith.stores;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static io.smallrye.common.constraint.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class StoreEntityIT {

    @Test
    @Transactional
    void testPersistAndRetrieve() {
        Store store = new Store("Store A");
        store.quantityProductsInStock = 5;

        store.persist();

        Store found = Store.findById(store.id);
        assertNotNull(found);
        assertEquals("Store A", found.name);
    }
}
