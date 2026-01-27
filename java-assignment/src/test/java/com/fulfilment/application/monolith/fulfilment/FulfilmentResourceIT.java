package com.fulfilment.application.monolith.fulfilment;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class FulfilmentResourceIT {

    @Inject
    EntityManager entityManager;

    @BeforeEach
    @Transactional
    public void setup() {
        entityManager.createNativeQuery("TRUNCATE TABLE fulfilment CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE product CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE store CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE warehouse CASCADE").executeUpdate();
        
        // Insert test data
        entityManager.createNativeQuery("INSERT INTO product(id, name, stock, price) VALUES (1, 'Product1', 10, 10.0)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO store(id, name, quantityProductsInStock) VALUES (1, 'Store1', 10)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO warehouse(id, businessUnitCode, location, capacity, stock) VALUES (1, 'BU1', 'Loc1', 100, 0)").executeUpdate();
    }

    @Test
    public void testCreateFulfilment() {
        String requestBody = "{\"productId\": 1, \"storeId\": 1, \"warehouseId\": 1}";

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when().post("/fulfilment")
            .then()
            .statusCode(201)
            .body("product.id", is(1))
            .body("store.id", is(1))
            .body("warehouse.id", is(1));
    }
}
