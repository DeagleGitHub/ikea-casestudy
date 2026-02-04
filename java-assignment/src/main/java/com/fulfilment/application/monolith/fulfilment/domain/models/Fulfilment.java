package com.fulfilment.application.monolith.fulfilment.domain.models;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fulfilment")
@Getter
@Setter
@NoArgsConstructor
public class Fulfilment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private DbWarehouse warehouse;

    public Fulfilment(Product product, Store store, DbWarehouse warehouse) {
        this.product = product;
        this.store = store;
        this.warehouse = warehouse;
    }
}
