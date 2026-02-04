package com.fulfilment.application.monolith.fulfilment.adapters.database;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FulfilmentRepository implements PanacheRepository<Fulfilment> {

    public boolean existsByProductAndStoreAndWarehouse(Product product, Store store, DbWarehouse warehouse) {
        return count("product = ?1 and store = ?2 and warehouse = ?3", product, store, warehouse) > 0;
    }

    public long countByProductAndStore(Product product, Store store) {
        return count("product = ?1 and store = ?2", product, store);
    }

    public boolean existsByStoreAndWarehouse(Store store, DbWarehouse warehouse) {
        return count("store = ?1 and warehouse = ?2", store, warehouse) > 0;
    }

    public long countDistinctWarehousesByStore(Store store) {
        return find("select count(distinct f.warehouse) from Fulfilment f where f.store = ?1", store)
                .project(Long.class)
                .singleResult();
    }

    public boolean existsByWarehouseAndProduct(DbWarehouse warehouse, Product product) {
        return count("warehouse = ?1 and product = ?2", warehouse, product) > 0;
    }

    public long countDistinctProductsByWarehouse(DbWarehouse warehouse) {
        return find("select count(distinct f.product) from Fulfilment f where f.warehouse = ?1", warehouse)
                .project(Long.class)
                .singleResult();
    }
}
