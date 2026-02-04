package com.fulfilment.application.monolith.stores;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Cacheable
@Getter
@Setter
@NoArgsConstructor
public class Store extends PanacheEntityBase {

  @Id @GeneratedValue private Long id;

  @Column(length = 40, unique = true)
  private String name;

  private int quantityProductsInStock;

  public Store(String name) {
    this.name = name;
  }
}
