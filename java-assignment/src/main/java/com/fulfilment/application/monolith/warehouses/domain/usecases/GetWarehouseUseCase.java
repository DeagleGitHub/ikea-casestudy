package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.GetWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class GetWarehouseUseCase implements GetWarehouseOperation {

  private final WarehouseStore warehouseStore;

  public GetWarehouseUseCase(WarehouseStore warehouseStore) {
    this.warehouseStore = warehouseStore;
  }

  @Override
  public List<Warehouse> all() {
    return warehouseStore.getAll();
  }

  @Override
  public Warehouse byBusinessUnitCode(String buCode) {
    var warehouse = warehouseStore.findByBusinessUnitCode(buCode);
    if (warehouse == null) {
      throw new NotFoundException("Warehouse not found: " + buCode);
    }
    return warehouse;
  }
}