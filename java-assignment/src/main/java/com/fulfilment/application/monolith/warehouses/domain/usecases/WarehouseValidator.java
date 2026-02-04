package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.location.LocationGateway;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class WarehouseValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseValidator.class);

    private final WarehouseStore warehouseStore;
    private final LocationGateway locationGateway;

    public WarehouseValidator(WarehouseStore warehouseStore, LocationGateway locationGateway) {
        this.warehouseStore = warehouseStore;
        this.locationGateway = locationGateway;
    }

    public void validateForCreation(Warehouse warehouse) {
        if (warehouseStore.findByBusinessUnitCode(warehouse.getBusinessUnitCode()) != null) {
            LOGGER.warn("Validation failed: BU Code [{}] already exists and is active.", warehouse.getBusinessUnitCode());
            throw new BadRequestException("Business Unit Code already exists and is active.");
        }
        validateConstraints(warehouse, 0, false);
    }

    public void validateForReplacement(Warehouse newWarehouse, Warehouse oldWarehouse) {
        boolean sameLocation = newWarehouse.getLocation().equals(oldWarehouse.getLocation());
        int capacityToExclude = sameLocation ? oldWarehouse.getCapacity() : 0;
        boolean excludeOldFromCount = sameLocation;
        
        validateConstraints(newWarehouse, capacityToExclude, excludeOldFromCount);
    }

    private void validateConstraints(Warehouse warehouse, int capacityToExclude, boolean excludeOldFromCount) {
        if (warehouse.getStock() > warehouse.getCapacity()) {
            LOGGER.warn("Validation failed: Stock ({}) exceeds Capacity ({}) for BU Code [{}].",
                    warehouse.getStock(), warehouse.getCapacity(), warehouse.getBusinessUnitCode());
            throw new BadRequestException("Stock cannot exceed warehouse capacity.");
        }

        var location = locationGateway.resolveByIdentifier(warehouse.getLocation()).orElseThrow(() -> {
            LOGGER.warn("Validation failed: Location [{}] does not exist.", warehouse.getLocation());
            return new BadRequestException("The specified location does not exist.");
        });

        long currentWarehousesInLocation = warehouseStore.countActiveByLocation(warehouse.getLocation());
        if (excludeOldFromCount) {
            currentWarehousesInLocation--;
        }

        long effectiveCount = currentWarehousesInLocation + 1;
        if (excludeOldFromCount) {
             effectiveCount--;
        }

        if (effectiveCount > location.maxNumberOfWarehouses()) {
            LOGGER.warn("Validation failed: Max warehouses ({}) reached for location [{}].",
                    location.maxNumberOfWarehouses(), warehouse.getLocation());
            throw new BadRequestException("Maximum number of warehouses reached for this location (" + location.maxNumberOfWarehouses() + ").");
        }

        int currentTotalCapacity = warehouseStore.calculateTotalCapacityByLocation(warehouse.getLocation());
        if (currentTotalCapacity - capacityToExclude + warehouse.getCapacity() > location.maxCapacity()) {
            LOGGER.warn("Validation failed: New capacity ({}) plus existing ({}) - excluded ({}) exceeds max allowed ({}) for location [{}].",
                    warehouse.getCapacity(), currentTotalCapacity, capacityToExclude, location.maxCapacity(), warehouse.getLocation());
            throw new BadRequestException("Warehouse capacity exceeds the maximum allowed for this location (" + location.maxCapacity() + ").");
        }
    }
}
