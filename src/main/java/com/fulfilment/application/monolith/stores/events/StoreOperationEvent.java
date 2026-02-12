package com.fulfilment.application.monolith.stores.events;

import com.fulfilment.application.monolith.stores.Store;
import lombok.Getter;

@Getter
public class StoreOperationEvent {

    private Store store;
    private String event;

    public StoreOperationEvent(Store store, String event) {
        this.store = store;
        this.event = event;
    }
}
