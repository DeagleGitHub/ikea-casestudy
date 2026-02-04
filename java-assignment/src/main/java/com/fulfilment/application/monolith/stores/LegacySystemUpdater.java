package com.fulfilment.application.monolith.stores;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class LegacySystemUpdater {

    private static final Logger LOGGER = Logger.getLogger(LegacySystemUpdater.class);

    @Inject
    LegacyStoreManagerGateway legacyStoreManagerGateway;

    public void onStoreCreated(@Observes(during = TransactionPhase.AFTER_SUCCESS) CreateStoreEvent event) {
        try {
            legacyStoreManagerGateway.createStoreOnLegacySystem(event.getStore());
        } catch (Exception e) {
            LOGGER.error("Failed to create store in legacy system after DB commit", e);
        }
    }

    public void onStoreUpdated(@Observes(during = TransactionPhase.AFTER_SUCCESS) UpdateStoreEvent event) {
        try {
            legacyStoreManagerGateway.updateStoreOnLegacySystem(event.getStore());
        } catch (Exception e) {
            LOGGER.error("Failed to update store in legacy system after DB commit", e);
        }
    }
}
