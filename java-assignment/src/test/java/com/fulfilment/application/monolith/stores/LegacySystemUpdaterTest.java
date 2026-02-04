package com.fulfilment.application.monolith.stores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LegacySystemUpdaterTest {

    @Mock
    private LegacyStoreManagerGateway legacyStoreManagerGateway;

    @InjectMocks
    private LegacySystemUpdater legacySystemUpdater;

    @Test
    @DisplayName("GIVEN a CreateStoreEvent WHEN onStoreCreated is called THEN it should call the gateway to create the store")
    void callGatewayOnStoreCreated() {
        Store store = new Store("Test Store");
        CreateStoreEvent event = new CreateStoreEvent(store);

        legacySystemUpdater.onStoreCreated(event);

        verify(legacyStoreManagerGateway).createStoreOnLegacySystem(store);
    }

    @Test
    @DisplayName("GIVEN an UpdateStoreEvent WHEN onStoreUpdated is called THEN it should call the gateway to update the store")
    void callGatewayOnStoreUpdated() {
        Store store = new Store("Test Store");
        UpdateStoreEvent event = new UpdateStoreEvent(store);

        legacySystemUpdater.onStoreUpdated(event);

        verify(legacyStoreManagerGateway).updateStoreOnLegacySystem(store);
    }

    @Test
    @DisplayName("GIVEN gateway throws exception on create WHEN onStoreCreated is called THEN it should handle the exception")
    void handleExceptionOnStoreCreated() {
        Store store = new Store("Test Store");
        CreateStoreEvent event = new CreateStoreEvent(store);
        doThrow(new RuntimeException("Legacy system down")).when(legacyStoreManagerGateway).createStoreOnLegacySystem(store);

        legacySystemUpdater.onStoreCreated(event);
        verify(legacyStoreManagerGateway).createStoreOnLegacySystem(store);
    }

    @Test
    @DisplayName("GIVEN gateway throws exception on update WHEN onStoreUpdated is called THEN it should handle the exception")
    void handleExceptionOnStoreUpdated() {
        Store store = new Store("Test Store");
        UpdateStoreEvent event = new UpdateStoreEvent(store);
        doThrow(new RuntimeException("Legacy system down")).when(legacyStoreManagerGateway).updateStoreOnLegacySystem(store);

        legacySystemUpdater.onStoreUpdated(event);
        verify(legacyStoreManagerGateway).updateStoreOnLegacySystem(store);
    }
}
