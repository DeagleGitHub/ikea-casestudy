package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.GetWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarehouseResourceImplTest {

    @Mock
    private GetWarehouseOperation getUseCase;
    @Mock
    private CreateWarehouseOperation createUseCase;
    @Mock
    private ReplaceWarehouseOperation replaceUseCase;
    @Mock
    private ArchiveWarehouseOperation archiveUseCase;

    @InjectMocks
    private WarehouseResourceImpl warehouseResource;

    @Test
    @DisplayName("GIVEN warehouses exist WHEN listing all THEN return mapped list")
    void listAllWarehousesSuccessfully() {
        Warehouse domainWarehouse = new Warehouse();
        domainWarehouse.setBusinessUnitCode("BU001");
        domainWarehouse.setLocation("LOC01");
        domainWarehouse.setCapacity(100);
        domainWarehouse.setStock(50);
        
        when(getUseCase.all()).thenReturn(Collections.singletonList(domainWarehouse));

        List<com.warehouse.api.beans.Warehouse> result = warehouseResource.listAllWarehousesUnits();

        assertEquals(1, result.size());
        assertEquals("BU001", result.get(0).getBusinessUnitCode());
    }

    @Test
    @DisplayName("GIVEN warehouse data WHEN creating THEN call create use case")
    void createWarehouseSuccessfully() {
        com.warehouse.api.beans.Warehouse dto = new com.warehouse.api.beans.Warehouse();
        dto.setBusinessUnitCode("BU001");

        warehouseResource.createANewWarehouseUnit(dto);

        verify(createUseCase).create(any(Warehouse.class));
    }

    @Test
    @DisplayName("GIVEN existing warehouse WHEN getting by ID THEN return mapped warehouse")
    void getWarehouseByIdSuccessfully() {

        String buCode = "BU001";
        Warehouse domainWarehouse = new Warehouse();
        domainWarehouse.setBusinessUnitCode(buCode);
        when(getUseCase.byBusinessUnitCode(buCode)).thenReturn(domainWarehouse);

        com.warehouse.api.beans.Warehouse result = warehouseResource.getAWarehouseUnitByID(buCode);

        assertEquals(buCode, result.getBusinessUnitCode());
    }

    @Test
    @DisplayName("GIVEN existing warehouse WHEN archiving THEN call archive use case")
    void archiveWarehouseSuccessfully() {
        String buCode = "BU001";
        Warehouse domainWarehouse = new Warehouse();
        when(getUseCase.byBusinessUnitCode(buCode)).thenReturn(domainWarehouse);

        warehouseResource.archiveAWarehouseUnitByID(buCode);

        verify(archiveUseCase).archive(domainWarehouse);
    }

    @Test
    @DisplayName("GIVEN warehouse data WHEN replacing THEN call replace use case")
    void replaceWarehouseSuccessfully() {
        String buCode = "BU001";
        com.warehouse.api.beans.Warehouse dto = new com.warehouse.api.beans.Warehouse();

        warehouseResource.replaceTheCurrentActiveWarehouse(buCode, dto);

        verify(replaceUseCase).replace(any(Warehouse.class));
    }
}
