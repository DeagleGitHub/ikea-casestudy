package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReplaceWarehouseUseCase.class);

  private final WarehouseStore warehouseStore;
  private final WarehouseValidator warehouseValidator;

  public ReplaceWarehouseUseCase(WarehouseStore warehouseStore, WarehouseValidator warehouseValidator) {
    this.warehouseStore = warehouseStore;
    this.warehouseValidator = warehouseValidator;
  }

  @Override
  public void replace(Warehouse newWarehouse) {
    LOGGER.info("Attempting to replace warehouse with BU Code: [{}]", newWarehouse.getBusinessUnitCode());

    var existing = warehouseStore.findByBusinessUnitCode(newWarehouse.getBusinessUnitCode());

    if (existing == null) {
      LOGGER.warn("Replacement failed: Active warehouse [{}] not found.", newWarehouse.getBusinessUnitCode());
      throw new NotFoundException("Active warehouse not found for replacement.");
    }

    if (!newWarehouse.getStock().equals(existing.getStock())) {
      LOGGER.warn("Replacement failed: Stock mismatch for BU Code [{}]. Existing: {}, New: {}",
          newWarehouse.getBusinessUnitCode(), existing.getStock(), newWarehouse.getStock());
      throw new BadRequestException("New warehouse stock must match existing stock: " + existing.getStock());
    }

    warehouseValidator.validateForReplacement(newWarehouse, existing);

    LOGGER.info("Archiving existing warehouse [{}] and creating the new version.", newWarehouse.getBusinessUnitCode());
    warehouseStore.remove(existing);

    warehouseStore.create(newWarehouse);

    LOGGER.info("Successfully replaced warehouse with BU Code: [{}]", newWarehouse.getBusinessUnitCode());
  }
}
