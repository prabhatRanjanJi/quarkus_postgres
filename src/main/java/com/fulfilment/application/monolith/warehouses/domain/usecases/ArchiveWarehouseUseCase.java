package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.domain.WarehouseValidator;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@ApplicationScoped
@RequiredArgsConstructor
public class ArchiveWarehouseUseCase implements ArchiveWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final WarehouseValidator warehouseValidator;

  @Override
  public void archive(Warehouse warehouse) {
    DbWarehouse dbWarehouse = warehouseValidator.validateArchive(warehouse.businessUnitCode);
    warehouseStore.archive(dbWarehouse);
  }

  @Override
  public void archiveById(String id) {
    DbWarehouse byWarehouseId = warehouseStore.findByWarehouseId(Long.valueOf(id));
    archive(byWarehouseId.toWarehouse());
  }
}
