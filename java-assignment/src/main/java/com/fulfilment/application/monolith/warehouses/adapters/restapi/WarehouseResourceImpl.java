package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.GetWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.warehouse.api.WarehouseResource;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@RequestScoped
public class WarehouseResourceImpl implements WarehouseResource {

  @Inject GetWarehouseOperation getUseCase;
  @Inject CreateWarehouseOperation createUseCase;
  @Inject ReplaceWarehouseOperation replaceUseCase;
  @Inject ArchiveWarehouseOperation archiveUseCase;

  @Override
  public List<Warehouse> listAllWarehousesUnits() {
    return getUseCase.all().stream().map(this::toWarehouseResponse).toList();
  }

  @Override
  public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
    createUseCase.create(toDomain(data));
    return data;
  }

  @Override
  public Warehouse getAWarehouseUnitByID(String businessUnitCode) {
    var warehouse = getUseCase.byBusinessUnitCode(businessUnitCode);
    return toWarehouseResponse(warehouse);
  }

  @Override
  public void archiveAWarehouseUnitByID(String businessUnitCode) {
    var warehouse = getUseCase.byBusinessUnitCode(businessUnitCode);
    archiveUseCase.archive(warehouse);
  }

  @Override
  public Warehouse replaceTheCurrentActiveWarehouse(String businessUnitCode, @NotNull Warehouse data) {
    data.setBusinessUnitCode(businessUnitCode);
    replaceUseCase.replace(toDomain(data));
    return data;
  }

  private Warehouse toWarehouseResponse(
      com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse) {
    var response = new Warehouse();
    response.setBusinessUnitCode(warehouse.businessUnitCode);
    response.setLocation(warehouse.location);
    response.setCapacity(warehouse.capacity);
    response.setStock(warehouse.stock);
    return response;
  }

  private com.fulfilment.application.monolith.warehouses.domain.models.Warehouse toDomain(Warehouse dto) {
    var domain = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
    domain.businessUnitCode = dto.getBusinessUnitCode();
    domain.location = dto.getLocation();
    domain.capacity = dto.getCapacity();
    domain.stock = dto.getStock();
    return domain;
  }
}
