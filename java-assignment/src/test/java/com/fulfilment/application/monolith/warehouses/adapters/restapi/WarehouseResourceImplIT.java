package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
class WarehouseResourceImplIT {

  @Inject
  EntityManager entityManager;

  @BeforeEach
  @Transactional
  public void resetWarehouseTable() {
    entityManager.createNativeQuery("TRUNCATE TABLE warehouse CASCADE").executeUpdate();
    entityManager.createNativeQuery("ALTER SEQUENCE warehouse_seq RESTART WITH 4").executeUpdate();

    entityManager.createNativeQuery("INSERT INTO warehouse(id, businessUnitCode, location, capacity, stock, createdAt, archivedAt) " +
        "VALUES (1, 'MWH.001', 'ZWOLLE-001', 100, 10, '2024-07-01', null)").executeUpdate();
    entityManager.createNativeQuery("INSERT INTO warehouse(id, businessUnitCode, location, capacity, stock, createdAt, archivedAt) " +
        "VALUES (2, 'MWH.012', 'AMSTERDAM-001', 50, 5, '2023-07-01', null)").executeUpdate();
    entityManager.createNativeQuery("INSERT INTO warehouse(id, businessUnitCode, location, capacity, stock, createdAt, archivedAt) " +
        "VALUES (3, 'MWH.023', 'TILBURG-001', 30, 27, '2021-02-01', null)").executeUpdate();
  }

  @Test
  @DisplayName("GIVEN existing warehouses WHEN get all warehouses THEN should return complete list")
  void whenGetAllWarehouses_ShouldReturnList() {
    given()
        .when().get("/warehouse")
        .then()
        .statusCode(200)
        .body("$", hasSize(3))
        .body("[0].businessUnitCode", is("MWH.001"))
        .body("[1].businessUnitCode", is("MWH.012"))
        .body("[2].businessUnitCode", is("MWH.023"));
  }

  @Test
  @DisplayName("GIVEN an existing business unit code WHEN get single warehouse THEN should return warehouse details")
  void whenGetSingleWarehouse_ShouldReturnDetails() {
    given()
        .when().get("/warehouse/MWH.001")
        .then()
        .statusCode(200)
        .body("businessUnitCode", is("MWH.001"))
        .body("location", is("ZWOLLE-001"))
        .body("capacity", is(100))
        .body("stock", is(10));
  }

  @Test
  @DisplayName("GIVEN a non-existent business unit code WHEN get single warehouse THEN should return 404 error")
  void whenGetNonExistentWarehouse_ShouldReturn404() {
    given()
        .when().get("/warehouse/NON-EXISTENT")
        .then()
        .statusCode(404);
  }

  @Test
  @DisplayName("GIVEN new warehouse data WHEN create warehouse THEN should persist correctly")
  void whenCreateWarehouse_ShouldPersist() {
    String newWarehouse = "{\"businessUnitCode\": \"MWH.099\", \"location\": \"EINDHOVEN-001\", \"capacity\": 70, \"stock\": 0}";

    given()
        .contentType(ContentType.JSON)
        .body(newWarehouse)
        .when().post("/warehouse")
        .then()
        .statusCode(200)
        .body("businessUnitCode", is("MWH.099"))
        .body("location", is("EINDHOVEN-001"));
  }

  @Test
  @DisplayName("GIVEN null data WHEN create warehouse THEN should return 400 error")
  void whenCreateWarehouseWithNullData_ShouldReturn400() {
    given()
        .contentType(ContentType.JSON)
        .when().post("/warehouse")
        .then()
        .statusCode(400);
  }

  @Test
  @DisplayName("GIVEN replacement data WHEN replace warehouse THEN should update and return new data")
  void whenReplaceWarehouse_ShouldUpdateValues() {
    String replacement = "{\"location\": \"ZWOLLE-001\", \"capacity\": 20, \"stock\": 10}";

    given()
        .contentType(ContentType.JSON)
        .body(replacement)
        .when().post("/warehouse/MWH.001/replacement")
        .then()
        .statusCode(200)
        .body("businessUnitCode", is("MWH.001"))
        .body("capacity", is(20))
        .body("stock", is(10));
  }

  @Test
  @DisplayName("GIVEN mismatching stock WHEN replace warehouse THEN should return 400 error")
  void whenReplaceWarehouseWithStockMismatch_ShouldReturn400() {
    String invalidReplacement = "{\"location\": \"ZWOLLE-001\", \"capacity\": 100, \"stock\": 20}";

    given()
        .contentType(ContentType.JSON)
        .body(invalidReplacement)
        .when().post("/warehouse/MWH.001/replacement")
        .then()
        .statusCode(400);
  }

  @Test
  @DisplayName("GIVEN replacement with capacity lower than existing stock WHEN replace warehouse THEN should return 400 error")
  void whenReplaceWarehouseWithInsufficientCapacity_ShouldReturn400() {
    String invalidReplacement = "{\"location\": \"ZWOLLE-001\", \"capacity\": 5, \"stock\": 10}";

    given()
        .contentType(ContentType.JSON)
        .body(invalidReplacement)
        .when().post("/warehouse/MWH.001/replacement")
        .then()
        .statusCode(400);
  }

  @Test
  @DisplayName("GIVEN a non-existent business unit code WHEN replace warehouse THEN should return 404 error")
  void whenReplaceNonExistentWarehouse_ShouldReturn404() {
    String replacement = "{\"location\": \"ZWOLLE-001\", \"capacity\": 100, \"stock\": 10}";

    given()
        .contentType(ContentType.JSON)
        .body(replacement)
        .when().post("/warehouse/NON-EXISTENT/replacement")
        .then()
        .statusCode(404);
  }

  @Test
  @DisplayName("GIVEN an existing warehouse code WHEN archive warehouse THEN should return 204 status")
  void whenArchiveWarehouse_ShouldReturnNoContent() {
    given()
        .when().delete("/warehouse/MWH.023")
        .then()
        .statusCode(204);

    given()
        .when().get("/warehouse/MWH.023")
        .then()
        .statusCode(404);
  }

  @Test
  @DisplayName("GIVEN a non-existent code WHEN archive warehouse THEN should return 404 error")
  void whenArchiveNonExistentWarehouse_ShouldReturn404() {
    given()
        .when().delete("/warehouse/NON-EXISTENT")
        .then()
        .statusCode(404);
  }
}