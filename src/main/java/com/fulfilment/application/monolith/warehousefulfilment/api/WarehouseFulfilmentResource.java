package com.fulfilment.application.monolith.warehousefulfilment.api;

import com.fulfilment.application.monolith.warehousefulfilment.implementation.WarehouseFulfilmentUseCase;
import com.fulfilment.application.monolith.warehousefulfilment.model.WarehouseFulfilment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

@Path("store")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
@RequiredArgsConstructor
public class WarehouseFulfilmentResource {

  private final WarehouseFulfilmentUseCase warehouseFulfilmentUseCase;


  @POST
  @Path("/{warehouseFulfilment}/createWarehouseFulfilment")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  WarehouseFulfilment createWarehouseFulfilment(@NotNull WarehouseFulfilment warehouseFulfilment) {
    warehouseFulfilmentUseCase.save(warehouseFulfilment);
    return warehouseFulfilment;
  }
}
