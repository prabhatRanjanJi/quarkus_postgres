package com.fulfilment.application.monolith.stores.service;

import com.fulfilment.application.monolith.StoreEnum;
import com.fulfilment.application.monolith.stores.LegacyStoreManagerGateway;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.stores.events.StoreOperationEvent;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@QuarkusTest
class StorePostOperationServiceTest {

    @Inject
    StorePostOperationService storePostOperationService;

    @InjectMock
    LegacyStoreManagerGateway legacyStoreManagerGateway;

    @Test
    void shouldCallLegacyCreateOnCreateEvent() {
        Store store = new Store();
        store.id = 1L;
        store.name = "Store-A";

        StoreOperationEvent event =
                new StoreOperationEvent(store, StoreEnum.CREATE.name());

        storePostOperationService.onStoreCreation(event);

        verify(legacyStoreManagerGateway).createStoreOnLegacySystem(store);
        verify(legacyStoreManagerGateway, never()).updateStoreOnLegacySystem(store);
    }

    @Test
    void shouldCallLegacyUpdateOnUpdateEvent() {
        Store store = new Store();
        store.id = 2L;
        store.name = "Store-B";

        StoreOperationEvent event =
                new StoreOperationEvent(store, StoreEnum.UPDATE.name());

        storePostOperationService.onStoreCreation(event);

        verify(legacyStoreManagerGateway).updateStoreOnLegacySystem(store);
        verify(legacyStoreManagerGateway, never()).createStoreOnLegacySystem(store);
    }

    @Test
    void shouldCallLegacyUpdateOnPatchEvent() {
        Store store = new Store();
        store.id = 3L;
        store.name = "Store-C";

        StoreOperationEvent event =
                new StoreOperationEvent(store, StoreEnum.PATCH.name());

        storePostOperationService.onStoreCreation(event);

        verify(legacyStoreManagerGateway).updateStoreOnLegacySystem(store);
        verify(legacyStoreManagerGateway, never()).createStoreOnLegacySystem(store);
    }

    @Test
    void shouldDoNothingForUnknownEvent() {
        Store store = new Store();
        store.id = 4L;
        store.name = "Store-D";

        StoreOperationEvent event =
                new StoreOperationEvent(store, "DELETE");

        storePostOperationService.onStoreCreation(event);

        verify(legacyStoreManagerGateway, never()).createStoreOnLegacySystem(store);
        verify(legacyStoreManagerGateway, never()).updateStoreOnLegacySystem(store);
    }
}
