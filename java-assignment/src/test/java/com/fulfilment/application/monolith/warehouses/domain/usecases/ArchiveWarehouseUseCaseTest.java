package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArchiveWarehouseUseCaseTest {

    @Mock
    private WarehouseStore warehouseStore;

    @InjectMocks
    private ArchiveWarehouseUseCase archiveWarehouseUseCase;

    @Test
    @DisplayName("GIVEN warehouse WHEN archiving THEN remove from store")
    void archiveWarehouseSuccessfully() {
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("BU001");

        archiveWarehouseUseCase.archive(warehouse);

        verify(warehouseStore).remove(warehouse);
    }
}
