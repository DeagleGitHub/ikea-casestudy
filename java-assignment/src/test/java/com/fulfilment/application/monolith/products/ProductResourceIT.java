package com.fulfilment.application.monolith.products;

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
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
class ProductResourceIT {

  @Inject
  EntityManager entityManager;

  @BeforeEach
  @Transactional
  public void resetProductTable() {
    entityManager.createNativeQuery("TRUNCATE TABLE product CASCADE").executeUpdate();
    entityManager.createNativeQuery("ALTER SEQUENCE product_seq RESTART WITH 4").executeUpdate();
    entityManager.createNativeQuery("INSERT INTO product(id, name, stock) VALUES (1, 'TONSTAD', 10)").executeUpdate();
    entityManager.createNativeQuery("INSERT INTO product(id, name, stock) VALUES (2, 'KALLAX', 5)").executeUpdate();
    entityManager.createNativeQuery("INSERT INTO product(id, name, stock) VALUES (3, 'BESTÅ', 3)").executeUpdate();
  }

  @Test
  @DisplayName("GIVEN existing products WHEN get all products THEN should return list ordered by name")
  void whenGetAllProducts_ShouldReturnOrderedList() {
    given()
        .when().get("/product")
        .then()
        .statusCode(200)
        .body("$", hasSize(3))
        .body("[0].name", is("BESTÅ"))
        .body("[1].name", is("KALLAX"))
        .body("[2].name", is("TONSTAD"));
  }

  @Test
  @DisplayName("GIVEN an existing product ID WHEN get single product THEN should return product details")
  void whenGetSingleProduct_ShouldReturnProductDetails() {
    given()
        .when().get("/product/1")
        .then()
        .statusCode(200)
        .body("name", is("TONSTAD"))
        .body("stock", is(10));
  }

  @Test
  @DisplayName("GIVEN a non-existent product ID WHEN get single product THEN should return 404 error")
  void whenGetNonExistentProduct_ShouldReturn404() {
    given()
        .when().get("/product/999")
        .then()
        .statusCode(404)
        .body("error", containsString("does not exist"));
  }

  @Test
  @DisplayName("GIVEN new product data WHEN create product THEN should persist correctly")
  void whenCreateProduct_ShouldPersist() {
    String newProduct = "{\"name\": \"MALM\", \"stock\": 20}";

    given()
        .contentType(ContentType.JSON)
        .body(newProduct)
        .when().post("/product")
        .then()
        .statusCode(201)
        .body("name", is("MALM"))
        .body("stock", is(20));
  }

  @Test
  @DisplayName("GIVEN an invalid product with ID WHEN create product THEN should return 422 error")
  void whenCreateProductWithId_ShouldReturn422() {
    String invalidProduct = "{\"id\": 99, \"name\": \"FAIL\", \"stock\": 10}";

    given()
        .contentType(ContentType.JSON)
        .body(invalidProduct)
        .when().post("/product")
        .then()
        .statusCode(422)
        .body("error", containsString("Id was invalidly set"));
  }

  @Test
  @DisplayName("GIVEN updated data WHEN update product THEN should change values")
  void whenUpdateProduct_ShouldUpdateValues() {
    String updatedProduct = "{\"name\": \"TONSTAD_UPDATED\", \"stock\": 15}";

    given()
        .contentType(ContentType.JSON)
        .body(updatedProduct)
        .when().put("/product/1")
        .then()
        .statusCode(200)
        .body("name", is("TONSTAD_UPDATED"))
        .body("stock", is(15));
  }

  @Test
  @DisplayName("GIVEN update data without name WHEN update product THEN should return 422 error")
  void whenUpdateProductWithoutName_ShouldReturn422() {
    String invalidUpdate = "{\"stock\": 15}";

    given()
        .contentType(ContentType.JSON)
        .body(invalidUpdate)
        .when().put("/product/1")
        .then()
        .statusCode(422)
        .body("error", containsString("Product Name was not set"));
  }

  @Test
  @DisplayName("GIVEN a non-existent product ID WHEN update product THEN should return 404 error")
  void whenUpdateNonExistentProduct_ShouldReturn404() {
    String updatedProduct = "{\"name\": \"NON_EXISTENT\", \"stock\": 10}";

    given()
        .contentType(ContentType.JSON)
        .body(updatedProduct)
        .when().put("/product/999")
        .then()
        .statusCode(404)
        .body("error", containsString("does not exist"));
  }

  @Test
  @DisplayName("GIVEN an existing product WHEN delete product THEN should return 204 status")
  void whenDeleteProduct_ShouldReturnNoContent() {
    given()
        .when().delete("/product/3")
        .then()
        .statusCode(204);

    given()
        .when().get("/product/3")
        .then()
        .statusCode(404);
  }

  @Test
  @DisplayName("GIVEN a non-existent product ID WHEN delete product THEN should return 404 error")
  void whenDeleteNonExistentProduct_ShouldReturn404() {
    given()
        .when().delete("/product/999")
        .then()
        .statusCode(404)
        .body("error", containsString("does not exist"));
  }
}