package com.fulfilment.application.monolith.stores;

import lombok.Getter;

@Getter
public class UpdateStoreEvent {

    private final Store store;

    public UpdateStoreEvent(Store store) {
        this.store = store;
    }
}
