package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.WarehouseValidator;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
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
  private final ReplaceWarehouseOperation replaceWarehouseOperation;

  @Override
  public List<Warehouse> listAllWarehousesUnits() {
    return warehouseRepository.getAll().stream().map(this::toWarehouseResponse).toList();
  }

  @Override
  public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
    warehouseValidator.validateWarehouseByBuCode(data.getBusinessUnitCode());
    com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse = convertWarehouseObject(data);
    warehouseValidator.validateWarehouse(warehouse);

    DbWarehouse dbWarehouse = warehouseRepository.createWarehouse(warehouse);
    return prepareDTOFromEntity(dbWarehouse);
  }

  private com.fulfilment.application.monolith.warehouses.domain.models.Warehouse convertWarehouseObject(Warehouse data) {
    com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse = new
            com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
    warehouse.businessUnitCode = data.getBusinessUnitCode();
    warehouse.location = data.getLocation();
    warehouse.capacity = data.getCapacity();
    warehouse.stock = data.getStock();
    warehouse.archivedAt = null;

    return warehouse;
  }

  private Warehouse prepareDTOFromEntity(DbWarehouse dbWarehouse) {
    Warehouse warehouse = new Warehouse();
    warehouse.setId(String.valueOf(dbWarehouse.id));
    warehouse.setBusinessUnitCode(dbWarehouse.businessUnitCode);
    warehouse.setLocation(dbWarehouse.location);
    warehouse.setCapacity(dbWarehouse.capacity);
    warehouse.setStock(dbWarehouse.stock);

    return warehouse;
  }

  @Override
  public Warehouse getAWarehouseUnitByID(String id) {
    DbWarehouse dbWarehouse = warehouseRepository.findByWarehouseId(Long.valueOf(id));
    return prepareDTOFromEntity(dbWarehouse);
  }

  @Override
  public void archiveAWarehouseUnitByID(String id) {
    DbWarehouse dbWarehouse = warehouseValidator.validateArchiveById(id);
    warehouseRepository.archive(dbWarehouse);
  }

  @Override
  public Warehouse replaceTheCurrentActiveWarehouse(String businessUnitCode, @NotNull Warehouse data) {
    DbWarehouse replacedWarehouse = replaceWarehouseOperation.replace(convertWarehouseObject(data));
    return prepareDTOFromEntity(replacedWarehouse);
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
