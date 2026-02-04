package com.fulfilment.application.monolith.fulfilment.domain.usecases;

import com.fulfilment.application.monolith.common.exception.BusinessRuleViolationException;
import com.fulfilment.application.monolith.common.exception.ResourceNotFoundException;
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
        Product product = findProductOrThrow(productId);
        Store store = findStoreOrThrow(storeId);
        DbWarehouse warehouse = findWarehouseOrThrow(warehouseId);

        validateFulfilmentUniqueness(product, store, warehouse);
        validateProductWarehousesLimit(product, store);
        validateStoreWarehousesLimit(store, warehouse);
        validateWarehouseProductsLimit(warehouse, product);

        Fulfilment fulfilment = new Fulfilment(product, store, warehouse);
        fulfilmentRepository.persist(fulfilment);
        return fulfilment;
    }

    private Product findProductOrThrow(Long productId) {
        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found");
        }
        return product;
    }

    private Store findStoreOrThrow(Long storeId) {
        Store store = Store.findById(storeId);
        if (store == null) {
            throw new ResourceNotFoundException("Store not found");
        }
        return store;
    }

    private DbWarehouse findWarehouseOrThrow(Long warehouseId) {
        DbWarehouse warehouse = warehouseRepository.findById(warehouseId);
        if (warehouse == null) {
            throw new ResourceNotFoundException("Warehouse not found");
        }
        return warehouse;
    }

    private void validateFulfilmentUniqueness(Product product, Store store, DbWarehouse warehouse) {
        if (fulfilmentRepository.existsByProductAndStoreAndWarehouse(product, store, warehouse)) {
            throw new BusinessRuleViolationException("Fulfilment already exists");
        }
    }

    private void validateProductWarehousesLimit(Product product, Store store) {
        long productWarehousesForStore = fulfilmentRepository.countByProductAndStore(product, store);
        if (productWarehousesForStore >= 2) {
            throw new BusinessRuleViolationException("Product is already fulfilled by 2 warehouses for this store");
        }
    }

    private void validateStoreWarehousesLimit(Store store, DbWarehouse warehouse) {
        boolean warehouseAlreadyAssociated = fulfilmentRepository.existsByStoreAndWarehouse(store, warehouse);
        if (!warehouseAlreadyAssociated) {
            long distinctWarehousesForStore = fulfilmentRepository.countDistinctWarehousesByStore(store);
            if (distinctWarehousesForStore >= 3) {
                throw new BusinessRuleViolationException("Store is already fulfilled by 3 different warehouses");
            }
        }
    }

    private void validateWarehouseProductsLimit(DbWarehouse warehouse, Product product) {
        boolean productAlreadyInWarehouse = fulfilmentRepository.existsByWarehouseAndProduct(warehouse, product);
        if (!productAlreadyInWarehouse) {
            long distinctProductsInWarehouse = fulfilmentRepository.countDistinctProductsByWarehouse(warehouse);
            if (distinctProductsInWarehouse >= 5) {
                throw new BusinessRuleViolationException("Warehouse already stores 5 types of products");
            }
        }
    }
}
