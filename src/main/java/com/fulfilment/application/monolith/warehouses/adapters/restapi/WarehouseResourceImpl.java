package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.WarehouseUtility;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.WarehouseValidator;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.usecases.ArchiveWarehouseUseCase;
import com.fulfilment.application.monolith.warehouses.domain.usecases.CreateWarehouseUseCase;
import com.fulfilment.application.monolith.warehouses.domain.usecases.ReplaceWarehouseUseCase;
import com.warehouse.api.WarehouseResource;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequestScoped
@RequiredArgsConstructor
public class WarehouseResourceImpl implements WarehouseResource {

  @Inject private WarehouseRepository warehouseRepository;
  private final WarehouseValidator warehouseValidator;
  private final CreateWarehouseUseCase createWarehouseUseCase;
  private final ArchiveWarehouseUseCase archiveWarehouseUseCase;
  private final ReplaceWarehouseUseCase replaceWarehouseUseCase;
  private final WarehouseUtility warehouseUtility;

  @Override
  public List<Warehouse> listAllWarehousesUnits() {
    return warehouseRepository.getAll().stream().map(this::toWarehouseResponse).toList();
  }

  @Override
  public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
    try {
      createWarehouseUseCase.create(warehouseUtility.convertWarehouseObject(data));
      data.setId(data.getId());
      return data;
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
  }

  @Override
  public Warehouse getAWarehouseUnitByID(String id) {
    DbWarehouse dbWarehouse = warehouseRepository.findByWarehouseId(Long.valueOf(id));
    return warehouseUtility.prepareDTOFromEntity(dbWarehouse);
  }

  @Override
  public void archiveAWarehouseUnitByID(String id) {
    archiveWarehouseUseCase.archiveById(id);
  }

  @Override
  public Warehouse replaceTheCurrentActiveWarehouse(String businessUnitCode, @NotNull Warehouse data) {
    DbWarehouse replacedWarehouse = replaceWarehouseUseCase.replace(warehouseUtility.convertWarehouseObject(data));
    return warehouseUtility.prepareDTOFromEntity(replacedWarehouse);
  }

  private Warehouse toWarehouseResponse(
          com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse) {
    var response = new Warehouse();
    response.setBusinessUnitCode(warehouse.businessUnitCode);
    response.setLocation(warehouse.location);
    response.setCapacity(warehouse.capacity);
    response.setStock(warehouse.stock);

    return response;
  }

}
