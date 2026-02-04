package com.fulfilment.application.monolith.stores;

import lombok.Getter;

@Getter
public class CreateStoreEvent {

    private final Store store;

    public CreateStoreEvent(Store store) {
        this.store = store;
    }
}
