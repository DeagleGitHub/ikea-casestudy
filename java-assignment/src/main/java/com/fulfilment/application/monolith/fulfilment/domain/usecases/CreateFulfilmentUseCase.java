package com.fulfilment.application.monolith.fulfilment.domain.usecases;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import com.fulfilment.application.monolith.fulfilment.adapters.database.FulfilmentRepository;
import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class CreateFulfilmentUseCase {

    @Inject
    FulfilmentRepository fulfilmentRepository;

    @Inject
    ProductRepository productRepository;

    @Inject
    WarehouseRepository warehouseRepository;

    @Transactional
    public Fulfilment create(Long productId, Long storeId, Long warehouseId) {
        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new WebApplicationException("Product not found", 404);
        }

        Store store = Store.findById(storeId);
        if (store == null) {
            throw new WebApplicationException("Store not found", 404);
        }

        DbWarehouse warehouse = warehouseRepository.findById(warehouseId);
        if (warehouse == null) {
            throw new WebApplicationException("Warehouse not found", 404);
        }

        if (fulfilmentRepository.find("product = ?1 and store = ?2 and warehouse = ?3", product, store, warehouse).firstResult() != null) {
             throw new WebApplicationException("Fulfilment already exists", 409);
        }

        long productWarehousesForStore = fulfilmentRepository.count("product = ?1 and store = ?2", product, store);
        if (productWarehousesForStore >= 2) {
            throw new WebApplicationException("Product is already fulfilled by 2 warehouses for this store", 400);
        }

        long distinctWarehousesForStore = fulfilmentRepository.find("select count(distinct f.warehouse) from Fulfilment f where f.store = ?1", store).project(Long.class).firstResult();
        
        boolean warehouseAlreadyAssociated = fulfilmentRepository.count("store = ?1 and warehouse = ?2", store, warehouse) > 0;
        
        if (!warehouseAlreadyAssociated && distinctWarehousesForStore >= 3) {
             throw new WebApplicationException("Store is already fulfilled by 3 different warehouses", 400);
        }

        long distinctProductsInWarehouse = fulfilmentRepository.find("select count(distinct f.product) from Fulfilment f where f.warehouse = ?1", warehouse).project(Long.class).firstResult();
        
        boolean productAlreadyInWarehouse = fulfilmentRepository.count("warehouse = ?1 and product = ?2", warehouse, product) > 0;
        
        if (!productAlreadyInWarehouse && distinctProductsInWarehouse >= 5) {
            throw new WebApplicationException("Warehouse already stores 5 types of products", 400);
        }

        Fulfilment fulfilment = new Fulfilment(product, store, warehouse);
        fulfilmentRepository.persist(fulfilment);
        return fulfilment;
    }
}
