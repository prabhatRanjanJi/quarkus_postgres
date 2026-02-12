package com.fulfilment.application.monolith.stores.service;

import com.fulfilment.application.monolith.StoreEnum;
import com.fulfilment.application.monolith.stores.LegacyStoreManagerGateway;
import com.fulfilment.application.monolith.stores.events.StoreOperationEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class StorePostOperationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorePostOperationService.class);

    @Inject
    LegacyStoreManagerGateway legacyStoreManagerGateway;


    public void onStoreCreation(@ObservesAsync StoreOperationEvent event) {
        if (StringUtils.equalsIgnoreCase(event.getEvent(), StoreEnum.CREATE.name())) {
            LOGGER.info("Publishing to Legacy Store after Store creation...");
            legacyStoreManagerGateway.createStoreOnLegacySystem(event.getStore());
        } else if (StringUtils.equalsIgnoreCase(event.getEvent(), StoreEnum.UPDATE.name())) {
            LOGGER.info("Publishing to Legacy Store after Store updation...");
            legacyStoreManagerGateway.updateStoreOnLegacySystem(event.getStore());
        } else if (StringUtils.equalsIgnoreCase(event.getEvent(), StoreEnum.PATCH.name())) {
            LOGGER.info("Publishing to Legacy Store after Store patch...");
            legacyStoreManagerGateway.updateStoreOnLegacySystem(event.getStore());
        }
    }

}
