package com.fulfilment.application.monolith.fulfilment.adapters.restapi;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import com.fulfilment.application.monolith.fulfilment.domain.usecases.CreateFulfilmentUseCase;
import com.fulfilment.application.monolith.fulfilment.domain.usecases.GetFulfilmentUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Path("/fulfilment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FulfilmentResource {

    @Inject
    CreateFulfilmentUseCase createFulfilmentUseCase;

    @Inject
    GetFulfilmentUseCase getFulfilmentUseCase;

    @POST
    public Response create(CreateFulfilmentRequest request) {
        if (request.getProductId() == null || request.getStoreId() == null || request.getWarehouseId() == null) {
            throw new WebApplicationException("productId, storeId and warehouseId are required", 400);
        }
        Fulfilment fulfilment = createFulfilmentUseCase.create(request.getProductId(), request.getStoreId(), request.getWarehouseId());
        return Response.status(Response.Status.CREATED).entity(fulfilment).build();
    }

    @GET
    public List<Fulfilment> getAll() {
        return getFulfilmentUseCase.getAll();
    }

    @Getter
    @Setter
    public static class CreateFulfilmentRequest {
        private Long productId;
        private Long storeId;
        private Long warehouseId;
    }
}
