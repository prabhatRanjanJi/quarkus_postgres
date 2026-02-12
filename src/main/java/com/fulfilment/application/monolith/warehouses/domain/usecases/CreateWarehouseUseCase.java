package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.WarehouseValidator;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@RequiredArgsConstructor
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateWarehouseUseCase.class);
  private final WarehouseStore warehouseStore;
  private final WarehouseValidator warehouseValidator;

  @Override
  public void create(Warehouse warehouse) throws Exception {
    // validate scenario or throw exception
    warehouseValidator.validateWarehouse(warehouse);

    // if all went well, create the warehouse
    warehouseStore.create(warehouse);
    LOGGER.info("Warehouse created...");
  }
}
