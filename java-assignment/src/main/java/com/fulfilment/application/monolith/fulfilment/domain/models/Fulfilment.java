package com.fulfilment.application.monolith.fulfilment.domain.models;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import jakarta.persistence.*;

@Entity
@Table(name = "fulfilment")
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

    public Fulfilment() {
    }

    public Fulfilment(Product product, Store store, DbWarehouse warehouse) {
        this.product = product;
        this.store = store;
        this.warehouse = warehouse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public DbWarehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(DbWarehouse warehouse) {
        this.warehouse = warehouse;
    }
}
