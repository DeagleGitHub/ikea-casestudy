package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReplaceWarehouseUseCaseTest {

    @Mock
    private WarehouseStore warehouseStore;

    @Mock
    private WarehouseValidator warehouseValidator;

    @InjectMocks
    private ReplaceWarehouseUseCase replaceWarehouseUseCase;

    @Test
    @DisplayName("GIVEN non-existent warehouse WHEN replacing THEN throw NotFoundException")
    void throwExceptionWhenWarehouseNotFound() {
        Warehouse newWarehouse = new Warehouse();
        newWarehouse.setBusinessUnitCode("BU001");
        when(warehouseStore.findByBusinessUnitCode(newWarehouse.getBusinessUnitCode())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> replaceWarehouseUseCase.replace(newWarehouse));
    }

    @Test
    @DisplayName("GIVEN stock mismatch WHEN replacing THEN throw BadRequestException")
    void throwExceptionWhenStockMismatch() {
        Warehouse newWarehouse = new Warehouse();
        newWarehouse.setBusinessUnitCode("BU001");
        newWarehouse.setStock(100);

        Warehouse existingWarehouse = new Warehouse();
        existingWarehouse.setBusinessUnitCode("BU001");
        existingWarehouse.setStock(50);

        when(warehouseStore.findByBusinessUnitCode(newWarehouse.getBusinessUnitCode())).thenReturn(existingWarehouse);

        assertThrows(BadRequestException.class, () -> replaceWarehouseUseCase.replace(newWarehouse));
    }

    @Test
    @DisplayName("GIVEN invalid replacement WHEN replacing THEN throw exception")
    void throwExceptionWhenValidationFails() {
        Warehouse newWarehouse = new Warehouse();
        newWarehouse.setBusinessUnitCode("BU001");
        newWarehouse.setStock(100);

        Warehouse existingWarehouse = new Warehouse();
        existingWarehouse.setBusinessUnitCode("BU001");
        existingWarehouse.setStock(100);

        when(warehouseStore.findByBusinessUnitCode(newWarehouse.getBusinessUnitCode())).thenReturn(existingWarehouse);
        doThrow(new BadRequestException("Invalid")).when(warehouseValidator).validateForReplacement(newWarehouse, existingWarehouse);

        assertThrows(BadRequestException.class, () -> replaceWarehouseUseCase.replace(newWarehouse));
        verify(warehouseStore, never()).remove(any());
        verify(warehouseStore, never()).create(any());
    }

    @Test
    @DisplayName("GIVEN valid replacement WHEN replacing THEN remove old and create new")
    void replaceWarehouseSuccessfully() {
        Warehouse newWarehouse = new Warehouse();
        newWarehouse.setBusinessUnitCode("BU001");
        newWarehouse.setStock(100);

        Warehouse existingWarehouse = new Warehouse();
        existingWarehouse.setBusinessUnitCode("BU001");
        existingWarehouse.setStock(100);

        when(warehouseStore.findByBusinessUnitCode(newWarehouse.getBusinessUnitCode())).thenReturn(existingWarehouse);

        replaceWarehouseUseCase.replace(newWarehouse);

        verify(warehouseValidator).validateForReplacement(newWarehouse, existingWarehouse);
        verify(warehouseStore).remove(existingWarehouse);
        verify(warehouseStore).create(newWarehouse);
    }
}
