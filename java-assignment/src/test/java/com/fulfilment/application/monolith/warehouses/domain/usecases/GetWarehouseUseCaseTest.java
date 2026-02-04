package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetWarehouseUseCaseTest {

    @Mock
    private WarehouseStore warehouseStore;

    @InjectMocks
    private GetWarehouseUseCase getWarehouseUseCase;

    @Test
    @DisplayName("GIVEN warehouses exist WHEN getting all THEN return list of warehouses")
    void getAllWarehousesSuccessfully() {
        Warehouse warehouse = new Warehouse();
        List<Warehouse> expectedList = Collections.singletonList(warehouse);
        when(warehouseStore.getAll()).thenReturn(expectedList);

        List<Warehouse> result = getWarehouseUseCase.all();

        assertEquals(expectedList, result);
    }

    @Test
    @DisplayName("GIVEN existing warehouse WHEN getting by BU code THEN return warehouse")
    void getWarehouseByBuCodeSuccessfully() {
        String buCode = "BU001";
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode(buCode);
        when(warehouseStore.findByBusinessUnitCode(buCode)).thenReturn(warehouse);

        Warehouse result = getWarehouseUseCase.byBusinessUnitCode(buCode);

        assertEquals(warehouse, result);
    }

    @Test
    @DisplayName("GIVEN non-existent warehouse WHEN getting by BU code THEN throw NotFoundException")
    void throwExceptionWhenWarehouseNotFound() {
        String buCode = "BU001";
        when(warehouseStore.findByBusinessUnitCode(buCode)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> getWarehouseUseCase.byBusinessUnitCode(buCode));
    }
}
