package com.fulfilment.application.monolith.warehousefulfilment.adapters.restapi;

import com.fulfilment.application.monolith.warehousefulfilment.domain.usecases.WarehouseFulfilmentUseCase;
import com.fulfilment.application.monolith.warehousefulfilment.domain.model.WarehouseFulfilment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import lombok.RequiredArgsConstructor;

@Path("store")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
@RequiredArgsConstructor
public class WarehouseFulfilmentResource {

  private final WarehouseFulfilmentUseCase warehouseFulfilmentUseCase;

  @POST
  @Path("/createWarehouseFulfilment")
  public WarehouseFulfilment createWarehouseFulfilment(
          @NotNull WarehouseFulfilment warehouseFulfilment) {
    warehouseFulfilmentUseCase.save(warehouseFulfilment);
    return warehouseFulfilment;
  }
}
