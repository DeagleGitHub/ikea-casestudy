package com.fulfilment.application.monolith.stores;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class StoreResourceIT {

  @Inject EntityManager entityManager;

  @InjectSpy
  LegacyStoreManagerGateway legacyStoreManagerGateway;

  @BeforeEach
  @Transactional
  public void resetStoreTable() {
    entityManager.createNativeQuery("TRUNCATE TABLE store CASCADE").executeUpdate();
    entityManager.createNativeQuery("ALTER SEQUENCE store_seq RESTART WITH 4").executeUpdate();
    entityManager.createNativeQuery("INSERT INTO store(id, name, quantityProductsInStock) VALUES (1, 'TONSTAD', 10)").executeUpdate();
    entityManager.createNativeQuery("INSERT INTO store(id, name, quantityProductsInStock) VALUES (2, 'KALLAX', 5)").executeUpdate();
    entityManager.createNativeQuery("INSERT INTO store(id, name, quantityProductsInStock) VALUES (3, 'BESTÅ', 3)").executeUpdate();
  }

  @Test
  @DisplayName("GIVEN existing stores WHEN get all stores THEN should return list ordered by name")
  void whenGetAllStores_ShouldReturnOrderedList() {
    given()
        .when().get("/store")
        .then()
        .statusCode(200)
        .body("$", hasSize(3))
        .body("[0].name", is("BESTÅ"))
        .body("[1].name", is("KALLAX"))
        .body("[2].name", is("TONSTAD"));
  }

  @Test
  @DisplayName("GIVEN an existing store ID WHEN get single store THEN should return store details")
  void whenGetSingleStore_ShouldReturnStoreDetails() {
    given()
        .when().get("/store/1")
        .then()
        .statusCode(200)
        .body("name", is("TONSTAD"))
        .body("quantityProductsInStock", is(10));
  }

  @Test
  @DisplayName("GIVEN a non-existent store ID WHEN get single store THEN should return 404 error")
  void whenGetNonExistentStore_ShouldReturn404() {
    given()
        .when().get("/store/999")
        .then()
        .statusCode(404)
        .body("error", containsString("does not exist"));
  }

  @Test
  @DisplayName("GIVEN new store data WHEN create store THEN should persist and notify legacy system")
  void whenCreateStore_ShouldPersistAndNotifyLegacy() {
    String newStore = "{\"name\": \"MALM\", \"quantityProductsInStock\": 20}";

    given()
        .contentType(ContentType.JSON)
        .body(newStore)
        .when().post("/store")
        .then()
        .statusCode(201);

    verify(legacyStoreManagerGateway, times(1)).createStoreOnLegacySystem(any(Store.class));
  }

  @Test
  @DisplayName("GIVEN updated data WHEN update store THEN should change values and notify legacy system")
  void whenUpdateStore_ShouldUpdateValuesAndNotifyLegacy() {
    String updatedStore = "{\"name\": \"TONSTAD_UPDATED\", \"quantityProductsInStock\": 15}";

    given()
        .contentType(ContentType.JSON)
        .body(updatedStore)
        .when().put("/store/1")
        .then()
        .statusCode(200);

    verify(legacyStoreManagerGateway, times(1)).updateStoreOnLegacySystem(any(Store.class));
  }

  @Test
  @DisplayName("GIVEN patch data WHEN patch store THEN should partial update and notify legacy system")
  void whenPatchStore_ShouldUpdateAndNotifyLegacy() {
    String patchedStore = "{\"name\": \"PATCHED\", \"quantityProductsInStock\": 50}";

    given()
        .contentType(ContentType.JSON)
        .body(patchedStore)
        .when().patch("/store/1")
        .then()
        .statusCode(200);

    verify(legacyStoreManagerGateway, times(1)).updateStoreOnLegacySystem(any(Store.class));
  }

  @Test
  @DisplayName("GIVEN an existing store WHEN delete store THEN should return 204 status")
  void whenDeleteStore_ShouldReturnNoContent() {
    given()
        .when().delete("/store/3")
        .then()
        .statusCode(204);

    given()
        .when().get("/store/3")
        .then()
        .statusCode(404);
  }

  @Test
  @DisplayName("GIVEN a database failure WHEN create store THEN should NOT notify legacy system")
  void whenTransactionFails_ShouldNotNotifyLegacy() {
    String invalidStore = "{\"id\": 99, \"name\": \"FAIL\", \"quantityProductsInStock\": 10}";

    given()
        .contentType(ContentType.JSON)
        .body(invalidStore)
        .when().post("/store")
        .then()
        .statusCode(422);

    verifyNoInteractions(legacyStoreManagerGateway);
  }
}