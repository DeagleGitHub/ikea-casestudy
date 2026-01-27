package com.fulfilment.application.monolith.fulfilment.adapters.restapi;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import com.fulfilment.application.monolith.fulfilment.domain.usecases.CreateFulfilmentUseCase;
import com.fulfilment.application.monolith.fulfilment.domain.usecases.GetFulfilmentUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
        if (request.productId == null || request.storeId == null || request.warehouseId == null) {
            throw new WebApplicationException("productId, storeId and warehouseId are required", 400);
        }
        Fulfilment fulfilment = createFulfilmentUseCase.create(request.productId, request.storeId, request.warehouseId);
        return Response.status(Response.Status.CREATED).entity(fulfilment).build();
    }

    @GET
    public List<Fulfilment> getAll() {
        return getFulfilmentUseCase.getAll();
    }

    public static class CreateFulfilmentRequest {
        public Long productId;
        public Long storeId;
        public Long warehouseId;
    }
}
