package com.fulfilment.application.monolith.stores.service;

import com.fulfilment.application.monolith.StoreEnum;
import com.fulfilment.application.monolith.stores.LegacyStoreManagerGateway;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.stores.StoreRepository;
import com.fulfilment.application.monolith.stores.events.StoreOperationEvent;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class StoreService {

    @Inject
    LegacyStoreManagerGateway legacyGateway;

    @Inject
    StoreRepository storeRepository;

    @Inject
    Event<StoreOperationEvent> eventAfterStoreProcessing;


    public Store getById(Long id) {
        return storeRepository.getById(id);
    }

    public Store create(Store store) {
        storeRepository.persist(store);
        eventAfterStoreProcessing.fireAsync(new StoreOperationEvent(store, StoreEnum.CREATE.name()));
        return store;
    }

    public Store update(Store updatedStore) {
        storeRepository.persist(updatedStore);
        eventAfterStoreProcessing.fireAsync(new StoreOperationEvent(updatedStore, StoreEnum.UPDATE.name()));
        return updatedStore;
    }

    public Store patch(Store updatedStore) {
        storeRepository.persist(updatedStore);
        eventAfterStoreProcessing.fireAsync(new StoreOperationEvent(updatedStore, StoreEnum.PATCH.name()));
        return updatedStore;
    }

    public void delete(Store store) {
        storeRepository.delete(store);
    }

    public List<Store> listAll() {
        return storeRepository.listAll(Sort.by("name"));
    }
}
