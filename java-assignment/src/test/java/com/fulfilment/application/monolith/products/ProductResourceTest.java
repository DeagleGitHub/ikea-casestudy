package com.fulfilment.application.monolith.products;

import io.quarkus.panache.common.Sort;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductResourceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductResource productResource;

    @Test
    @DisplayName("GIVEN products exist WHEN getting all THEN return sorted list")
    void getAllProductsSuccessfully() {
        Product product = new Product();
        List<Product> expectedList = Collections.singletonList(product);
        when(productRepository.listAll(any(Sort.class))).thenReturn(expectedList);

        List<Product> result = productResource.get();

        assertEquals(expectedList, result);
    }

    @Test
    @DisplayName("GIVEN existing product WHEN getting single THEN return product")
    void getSingleProductSuccessfully() {
        Long id = 1L;
        Product product = new Product();
        when(productRepository.findById(id)).thenReturn(product);

        Product result = productResource.getSingle(id);

        assertEquals(product, result);
    }

    @Test
    @DisplayName("GIVEN non-existent product WHEN getting single THEN throw 404")
    void throw404WhenProductNotFound() {
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(null);

        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> productResource.getSingle(id));
        assertEquals(404, exception.getResponse().getStatus());
    }

    @Test
    @DisplayName("GIVEN product with ID WHEN creating THEN throw 422")
    void throw422WhenCreatingProductWithId() {
        Product product = new Product();
        product.setId(1L);

        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> productResource.create(product));
        assertEquals(422, exception.getResponse().getStatus());
    }

    @Test
    @DisplayName("GIVEN valid product WHEN creating THEN persist and return 201")
    void createProductSuccessfully() {
        Product product = new Product();
        
        Response response = productResource.create(product);

        assertEquals(201, response.getStatus());
        verify(productRepository).persist(product);
    }

    @Test
    @DisplayName("GIVEN update with null name WHEN updating THEN throw 422")
    void throw422WhenUpdatingWithNullName() {
        Product product = new Product();
        product.setName(null);

        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> productResource.update(1L, product));
        assertEquals(422, exception.getResponse().getStatus());
    }

    @Test
    @DisplayName("GIVEN non-existent product WHEN updating THEN throw 404")
    void throw404WhenUpdatingNonExistentProduct() {
        Long id = 1L;
        Product product = new Product();
        product.setName("Name");
        when(productRepository.findById(id)).thenReturn(null);

        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> productResource.update(id, product));
        assertEquals(404, exception.getResponse().getStatus());
    }

    @Test
    @DisplayName("GIVEN valid update WHEN updating THEN update entity")
    void updateProductSuccessfully() {
        Long id = 1L;
        Product product = new Product();
        product.setName("New Name");
        
        Product existingProduct = new Product();
        existingProduct.setName("Old Name");
        
        when(productRepository.findById(id)).thenReturn(existingProduct);

        Product result = productResource.update(id, product);

        assertEquals("New Name", result.getName());
        verify(productRepository).persist(existingProduct);
    }

    @Test
    @DisplayName("GIVEN non-existent product WHEN deleting THEN throw 404")
    void throw404WhenDeletingNonExistentProduct() {
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(null);

        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> productResource.delete(id));
        assertEquals(404, exception.getResponse().getStatus());
    }

    @Test
    @DisplayName("GIVEN existing product WHEN deleting THEN delete entity")
    void deleteProductSuccessfully() {
        Long id = 1L;
        Product product = new Product();
        when(productRepository.findById(id)).thenReturn(product);

        Response response = productResource.delete(id);

        assertEquals(204, response.getStatus());
        verify(productRepository).delete(product);
    }
}
