package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse")
@Cacheable
@Getter
@Setter
@NoArgsConstructor
public class DbWarehouse {

  @Id @GeneratedValue private Long id;

  private String businessUnitCode;

  private String location;

  private Integer capacity;

  private Integer stock;

  private LocalDateTime createdAt;

  private LocalDateTime archivedAt;

  public Warehouse toWarehouse() {
    var warehouse = new Warehouse();
    warehouse.setBusinessUnitCode(this.businessUnitCode);
    warehouse.setLocation(this.location);
    warehouse.setCapacity(this.capacity);
    warehouse.setStock(this.stock);
    warehouse.setCreatedAt(this.createdAt);
    warehouse.setArchivedAt(this.archivedAt);
    return warehouse;
  }
}
