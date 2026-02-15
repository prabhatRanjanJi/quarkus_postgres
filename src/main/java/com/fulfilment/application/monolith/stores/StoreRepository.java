package com.fulfilment.application.monolith.stores;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class StoreRepository implements PanacheRepository<Store> {

    public Store getById(Long id) {
        Optional<Store> storeObj = find("id", id).firstResultOptional();
        if (storeObj.isPresent()) {
            return storeObj.get();
        }
        return null;
    }
}
