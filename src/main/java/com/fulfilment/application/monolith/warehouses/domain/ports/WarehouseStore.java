package com.fulfilment.application.monolith.warehouses.domain.ports;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

import java.util.List;

public interface WarehouseStore {

  List<Warehouse> getAll();

  DbWarehouse create(Warehouse warehouse);

  void update(Warehouse warehouse);

  void archive(DbWarehouse warehouse);

  void remove(Warehouse warehouse);

  Warehouse findByBusinessUnitCode(String buCode);

 List<DbWarehouse> getWarehouseListByLocation(String locationIdentifier);

  List<DbWarehouse> getActiveWarehouseListByLocation(String locationIdentifier);

  DbWarehouse findEntityByBusinessUnitCode(String businessUnitCode);

  List<DbWarehouse> findEntityListByBusinessUnitCode(String businessUnitCode);

  DbWarehouse findActiveEntityByBusinessUnitCode(String businessUnitCode);

  DbWarehouse findByWarehouseId(Long id);
}
