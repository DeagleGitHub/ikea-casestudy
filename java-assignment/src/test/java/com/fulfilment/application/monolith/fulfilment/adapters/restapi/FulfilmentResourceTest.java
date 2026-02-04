package com.fulfilment.application.monolith.fulfilment.adapters.restapi;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import com.fulfilment.application.monolith.fulfilment.domain.usecases.CreateFulfilmentUseCase;
import com.fulfilment.application.monolith.fulfilment.domain.usecases.GetFulfilmentUseCase;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FulfilmentResourceTest {

    @Mock
    private CreateFulfilmentUseCase createFulfilmentUseCase;

    @Mock
    private GetFulfilmentUseCase getFulfilmentUseCase;

    @InjectMocks
    private FulfilmentResource fulfilmentResource;

    @Test
    @DisplayName("GIVEN valid request WHEN creating THEN return 201 and fulfilment")
    void createFulfilmentSuccessfully() {
        FulfilmentResource.CreateFulfilmentRequest request = new FulfilmentResource.CreateFulfilmentRequest();
        request.setProductId(1L);
        request.setStoreId(1L);
        request.setWarehouseId(1L);

        Fulfilment fulfilment = new Fulfilment();
        when(createFulfilmentUseCase.create(1L, 1L, 1L)).thenReturn(fulfilment);

        Response response = fulfilmentResource.create(request);

        assertEquals(201, response.getStatus());
        assertEquals(fulfilment, response.getEntity());
    }

    @Test
    @DisplayName("GIVEN invalid request WHEN creating THEN throw 400")
    void throw400WhenRequestIsInvalid() {
        FulfilmentResource.CreateFulfilmentRequest request = new FulfilmentResource.CreateFulfilmentRequest();
        // Missing fields

        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> fulfilmentResource.create(request));
        assertEquals(400, exception.getResponse().getStatus());
    }

    @Test
    @DisplayName("GIVEN fulfilments exist WHEN getting all THEN return list")
    void getAllFulfilmentsSuccessfully() {
        List<Fulfilment> expectedList = Collections.singletonList(new Fulfilment());
        when(getFulfilmentUseCase.getAll()).thenReturn(expectedList);

        List<Fulfilment> result = fulfilmentResource.getAll();

        assertEquals(expectedList, result);
    }
}
