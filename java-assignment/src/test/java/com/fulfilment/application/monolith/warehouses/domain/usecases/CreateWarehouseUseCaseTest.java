package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.ws.rs.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateWarehouseUseCaseTest {

    @Mock
    private WarehouseStore warehouseStore;

    @Mock
    private WarehouseValidator warehouseValidator;

    @InjectMocks
    private CreateWarehouseUseCase createWarehouseUseCase;

    @Test
    @DisplayName("GIVEN valid warehouse WHEN creating THEN validate and create")
    void createWarehouseSuccessfully() {
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("BU001");

        createWarehouseUseCase.create(warehouse);

        verify(warehouseValidator).validateForCreation(warehouse);
        verify(warehouseStore).create(warehouse);
    }

    @Test
    @DisplayName("GIVEN invalid warehouse WHEN creating THEN throw exception and do not create")
    void throwExceptionWhenValidationFails() {
        Warehouse warehouse = new Warehouse();
        doThrow(new BadRequestException("Invalid")).when(warehouseValidator).validateForCreation(warehouse);

        assertThrows(BadRequestException.class, () -> createWarehouseUseCase.create(warehouse));
        verify(warehouseStore, never()).create(warehouse);
    }
}
