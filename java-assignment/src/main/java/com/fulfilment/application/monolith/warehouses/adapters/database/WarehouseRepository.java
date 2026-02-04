package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

  @Override
  public List<Warehouse> getAll() {
    return this.listAll().stream().map(DbWarehouse::toWarehouse).toList();
  }

  @Override
  @Transactional
  public void create(Warehouse warehouse) {
    DbWarehouse dbEntity = fromWarehouse(warehouse);
    dbEntity.setCreatedAt(LocalDateTime.now());
    this.persist(dbEntity);
  }

  @Override
  @Transactional
  public void remove(Warehouse warehouse) {
    this.update("archivedAt = ?1 where businessUnitCode = ?2", LocalDateTime.now(), warehouse.getBusinessUnitCode());
  }

  @Override
  public Warehouse findByBusinessUnitCode(String buCode) {
    return this.find("businessUnitCode = ?1 and archivedAt IS NULL", buCode)
        .firstResultOptional()
        .map(DbWarehouse::toWarehouse)
        .orElse(null);
  }

  @Override
  public long countActiveByLocation(String location) {
    return this.count("location = ?1 and archivedAt IS NULL", location);
  }

  @Override
  public int calculateTotalCapacityByLocation(String location) {
    Long sum = this.find("select sum(capacity) from DbWarehouse where location = ?1 and archivedAt IS NULL", location)
            .project(Long.class)
            .firstResult();
    return sum != null ? sum.intValue() : 0;
  }

  private DbWarehouse fromWarehouse(Warehouse warehouse) {
    DbWarehouse db = new DbWarehouse();
    db.setBusinessUnitCode(warehouse.getBusinessUnitCode());
    db.setLocation(warehouse.getLocation());
    db.setCapacity(warehouse.getCapacity());
    db.setStock(warehouse.getStock());
    return db;
  }
}
