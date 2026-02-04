package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.location.LocationGateway;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.ws.rs.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarehouseValidatorTest {

    @Mock
    private WarehouseStore warehouseStore;

    @Mock
    private LocationGateway locationGateway;

    @InjectMocks
    private WarehouseValidator warehouseValidator;

    @Test
    @DisplayName("GIVEN existing BU code WHEN validating for creation THEN throw BadRequestException")
    void throwExceptionWhenBuCodeExists() {
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("BU001");
        when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(warehouse);

        assertThrows(BadRequestException.class, () -> warehouseValidator.validateForCreation(warehouse));
    }

    @Test
    @DisplayName("GIVEN stock exceeds capacity WHEN validating for creation THEN throw BadRequestException")
    void throwExceptionWhenStockExceedsCapacity() {
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("BU001");
        warehouse.setCapacity(100);
        warehouse.setStock(150);
        when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(null);

        assertThrows(BadRequestException.class, () -> warehouseValidator.validateForCreation(warehouse));
    }

    @Test
    @DisplayName("GIVEN non-existent location WHEN validating for creation THEN throw BadRequestException")
    void throwExceptionWhenLocationDoesNotExist() {
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("BU001");
        warehouse.setCapacity(100);
        warehouse.setStock(50);
        warehouse.setLocation("LOC001");
        
        when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(null);
        when(locationGateway.resolveByIdentifier("LOC001")).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> warehouseValidator.validateForCreation(warehouse));
    }

    @Test
    @DisplayName("GIVEN max warehouses reached WHEN validating for creation THEN throw BadRequestException")
    void throwExceptionWhenMaxWarehousesReached() {
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("BU001");
        warehouse.setCapacity(100);
        warehouse.setStock(50);
        warehouse.setLocation("LOC001");
        
        Location location = new Location("LOC001", 2, 1000);
        
        when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(null);
        when(locationGateway.resolveByIdentifier("LOC001")).thenReturn(Optional.of(location));
        when(warehouseStore.countActiveByLocation("LOC001")).thenReturn(2L);

        assertThrows(BadRequestException.class, () -> warehouseValidator.validateForCreation(warehouse));
    }

    @Test
    @DisplayName("GIVEN max capacity reached WHEN validating for creation THEN throw BadRequestException")
    void throwExceptionWhenMaxCapacityReached() {
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("BU001");
        warehouse.setCapacity(100);
        warehouse.setStock(50);
        warehouse.setLocation("LOC001");
        
        Location location = new Location("LOC001", 5, 200);
        
        when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(null);
        when(locationGateway.resolveByIdentifier("LOC001")).thenReturn(Optional.of(location));
        when(warehouseStore.countActiveByLocation("LOC001")).thenReturn(1L);
        when(warehouseStore.calculateTotalCapacityByLocation("LOC001")).thenReturn(150);

        assertThrows(BadRequestException.class, () -> warehouseValidator.validateForCreation(warehouse));
    }

    @Test
    @DisplayName("GIVEN valid data WHEN validating for creation THEN success")
    void validateCreationSuccessfully() {
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("BU001");
        warehouse.setCapacity(100);
        warehouse.setStock(50);
        warehouse.setLocation("LOC001");
        
        Location location = new Location("LOC001", 5, 1000);
        
        when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(null);
        when(locationGateway.resolveByIdentifier("LOC001")).thenReturn(Optional.of(location));
        when(warehouseStore.countActiveByLocation("LOC001")).thenReturn(1L);
        when(warehouseStore.calculateTotalCapacityByLocation("LOC001")).thenReturn(150);

        assertDoesNotThrow(() -> warehouseValidator.validateForCreation(warehouse));
    }
    
    @Test
    @DisplayName("GIVEN valid replacement in same location WHEN validating for replacement THEN success")
    void validateReplacementSameLocationSuccessfully() {
        Warehouse oldWarehouse = new Warehouse();
        oldWarehouse.setBusinessUnitCode("BU001");
        oldWarehouse.setLocation("LOC001");
        oldWarehouse.setCapacity(100);
        
        Warehouse newWarehouse = new Warehouse();
        newWarehouse.setBusinessUnitCode("BU001");
        newWarehouse.setLocation("LOC001");
        newWarehouse.setCapacity(120);
        newWarehouse.setStock(50);
        
        Location location = new Location("LOC001", 5, 1000);
        
        when(locationGateway.resolveByIdentifier("LOC001")).thenReturn(Optional.of(location));
        when(warehouseStore.countActiveByLocation("LOC001")).thenReturn(1L);
        when(warehouseStore.calculateTotalCapacityByLocation("LOC001")).thenReturn(100);

        assertDoesNotThrow(() -> warehouseValidator.validateForReplacement(newWarehouse, oldWarehouse));
    }
    
    @Test
    @DisplayName("GIVEN capacity exceeded in replacement same location WHEN validating for replacement THEN throw BadRequestException")
    void throwExceptionWhenCapacityExceededInReplacement() {
        Warehouse oldWarehouse = new Warehouse();
        oldWarehouse.setBusinessUnitCode("BU001");
        oldWarehouse.setLocation("LOC001");
        oldWarehouse.setCapacity(100);
        
        Warehouse newWarehouse = new Warehouse();
        newWarehouse.setBusinessUnitCode("BU001");
        newWarehouse.setLocation("LOC001");
        newWarehouse.setCapacity(200);
        newWarehouse.setStock(50);
        
        Location location = new Location("LOC001", 5, 250);
        
        when(locationGateway.resolveByIdentifier("LOC001")).thenReturn(Optional.of(location));
        when(warehouseStore.countActiveByLocation("LOC001")).thenReturn(1L);
        when(warehouseStore.calculateTotalCapacityByLocation("LOC001")).thenReturn(200);

        assertThrows(BadRequestException.class, () -> warehouseValidator.validateForReplacement(newWarehouse, oldWarehouse));
    }
}
