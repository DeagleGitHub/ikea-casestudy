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

  public ReplaceWarehouseUseCase(WarehouseStore warehouseStore) {
    this.warehouseStore = warehouseStore;
  }

  @Override
  public void replace(Warehouse newWarehouse) {
    LOGGER.info("Attempting to replace warehouse with BU Code: [{}]", newWarehouse.businessUnitCode);

    var existing = warehouseStore.findByBusinessUnitCode(newWarehouse.businessUnitCode);

    if (existing == null) {
      LOGGER.warn("Replacement failed: Active warehouse [{}] not found.", newWarehouse.businessUnitCode);
      throw new NotFoundException("Active warehouse not found for replacement.");
    }

    if (!newWarehouse.stock.equals(existing.stock)) {
      LOGGER.warn("Replacement failed: Stock mismatch for BU Code [{}]. Existing: {}, New: {}",
          newWarehouse.businessUnitCode, existing.stock, newWarehouse.stock);
      throw new BadRequestException("New warehouse stock must match existing stock: " + existing.stock);
    }

    if (newWarehouse.capacity < existing.stock) {
      LOGGER.warn("Replacement failed: New capacity ({}) cannot handle current stock ({}) for BU Code [{}].",
          newWarehouse.capacity, existing.stock, newWarehouse.businessUnitCode);
      throw new BadRequestException("New capacity cannot handle current stock.");
    }

    LOGGER.info("Archiving existing warehouse [{}] and creating the new version.", newWarehouse.businessUnitCode);
    warehouseStore.remove(existing);

    warehouseStore.create(newWarehouse);

    LOGGER.info("Successfully replaced warehouse with BU Code: [{}]", newWarehouse.businessUnitCode);
  }
}