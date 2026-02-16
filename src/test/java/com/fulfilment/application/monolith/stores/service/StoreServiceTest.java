package com.fulfilment.application.monolith.stores.service;

import com.fulfilment.application.monolith.stores.LegacyStoreManagerGateway;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.stores.StoreRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class StoreServiceTest {

    @Inject
    StoreService storeService;

    @InjectMock
    StoreRepository storeRepository;

    @InjectMock
    LegacyStoreManagerGateway legacyGateway;

    @Test
    void shouldGetStoreById() {
        Store store = new Store();
        store.id = 1L;
        store.name = "Test-Store";

        when(storeRepository.getById(1L)).thenReturn(store);

        Store result = storeService.getById(1L);

        assertEquals("Test-Store", result.name);
        verify(storeRepository).getById(1L);
    }

    @Test
    void shouldCreateStoreSuccessfully() {
        Store store = new Store();
        store.name = "New-Store";
        store.quantityProductsInStock = 20;

        Store result = storeService.create(store);

        assertEquals("New-Store", result.name);
        verify(storeRepository).persist(store);
    }

    @Test
    void shouldUpdateStoreSuccessfully() {
        Store store = new Store();
        store.id = 2L;
        store.name = "Updated-Store";
        store.quantityProductsInStock = 50;

        Store result = storeService.update(store);

        assertEquals("Updated-Store", result.name);
        verify(storeRepository).persist(store);
    }

    @Test
    void shouldPatchStoreSuccessfully() {
        Store store = new Store();
        store.id = 3L;
        store.name = "Patched-Store";
        store.quantityProductsInStock = 70;

        Store result = storeService.patch(store);

        assertEquals("Patched-Store", result.name);
        verify(storeRepository).persist(store);
    }

    @Test
    void shouldDeleteStoreSuccessfully() {
        Store store = new Store();
        store.id = 4L;

        storeService.delete(store);

        verify(storeRepository).delete(store);
    }

    @Test
    void shouldListAllStores() {
        Store s1 = new Store();
        s1.name = "A";
        Store s2 = new Store();
        s2.name = "B";

        when(storeRepository.listAll(any())).thenReturn(List.of(s1, s2));

        List<Store> result = storeService.listAll();

        assertEquals(2, result.size());
        verify(storeRepository).listAll(any());
    }
}
