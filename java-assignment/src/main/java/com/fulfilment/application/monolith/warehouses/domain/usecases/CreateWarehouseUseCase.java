package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.location.LocationGateway;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateWarehouseUseCase.class);

  private final WarehouseStore warehouseStore;
  private final LocationGateway locationGateway;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore, LocationGateway locationGateway) {
    this.warehouseStore = warehouseStore;
    this.locationGateway = locationGateway;
  }

  @Override
  public void create(Warehouse warehouse) {
    LOGGER.info("Attempting to create warehouse with BU Code: [{}] at Location: [{}]",
        warehouse.businessUnitCode, warehouse.location);

    if (warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode) != null) {
      LOGGER.warn("Creation failed: BU Code [{}] already exists and is active.", warehouse.businessUnitCode);
      throw new BadRequestException("Business Unit Code already exists and is active.");
    }

    if (warehouse.stock > warehouse.capacity) {
      LOGGER.warn("Creation failed: Stock ({}) exceeds Capacity ({}) for BU Code [{}].",
          warehouse.stock, warehouse.capacity, warehouse.businessUnitCode);
      throw new BadRequestException("Stock cannot exceed warehouse capacity.");
    }

    var location = locationGateway.resolveByIdentifier(warehouse.location);
    if (location == null) {
      LOGGER.warn("Creation failed: Location [{}] does not exist.", warehouse.location);
      throw new BadRequestException("The specified location does not exist.");
    }

    long currentWarehousesInLocation = warehouseStore.countActiveByLocation(warehouse.location);

    if (currentWarehousesInLocation >= location.maxNumberOfWarehouses) {
      LOGGER.warn("Creation failed: Max warehouses ({}) reached for location [{}].",
          location.maxNumberOfWarehouses, warehouse.location);
      throw new BadRequestException("Maximum number of warehouses reached for this location (" + location.maxNumberOfWarehouses + ").");
    }

    if (warehouse.capacity > location.maxCapacity) {
      LOGGER.warn("Creation failed: Capacity ({}) exceeds max allowed ({}) for location [{}].",
          warehouse.capacity, location.maxCapacity, warehouse.location);
      throw new BadRequestException("Warehouse capacity exceeds the maximum allowed for this location (" + location.maxCapacity + ").");
    }

    warehouseStore.create(warehouse);
    LOGGER.info("Successfully created warehouse with BU Code: [{}]", warehouse.businessUnitCode);
  }
}