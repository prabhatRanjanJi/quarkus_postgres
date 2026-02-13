package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.exception.InvalidInputException;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

  @Override
  public List<Warehouse> getAll() {
    return this.listAll().stream().map(DbWarehouse::toWarehouse).toList();
  }

  @Override
  public DbWarehouse create(Warehouse warehouse) {
    if (null == warehouse) {
      throw new InvalidInputException("Warehouse object can not be 'null'.");
    }
    DbWarehouse dbWarehouse = findActiveEntityByBusinessUnitCode(warehouse.businessUnitCode);


    if (null != dbWarehouse) {
      throw new InvalidInputException("Warehouse with Business Code " + warehouse.businessUnitCode + " already exists.");
    }

    return createWarehouse(warehouse);
  }

  public DbWarehouse createWarehouse(Warehouse warehouse) {
    DbWarehouse warehouseEntity = createWareHouseEntityFromObj(warehouse);
    persist(warehouseEntity);
    warehouse.setId(warehouse.getId());
    return warehouseEntity;
  }

  @Override
  public void update(Warehouse warehouse) {
    if (null == warehouse) {
      throw new InvalidInputException("Warehouse can not be 'null'.");
    } else {
      DbWarehouse warehouseEntity = findActiveEntityByBusinessUnitCode(warehouse.businessUnitCode);
      if (null != warehouseEntity) {
        warehouseEntity.location = warehouse.location;
        warehouseEntity.capacity = warehouse.capacity;
        warehouseEntity.stock = warehouse.stock;
        warehouseEntity.archivedAt = warehouse.archivedAt;

        persist(warehouseEntity);
      } else {
        throw new InvalidInputException("Can not update as Warehouse with Business Unit Code " + warehouse.businessUnitCode + " is not present.");
      }
    }
  }

  @Override
  public void archive(DbWarehouse warehouse) {
    warehouse.archivedAt = LocalDateTime.now();
    persist(warehouse);
  }

  @Override
  public void remove(Warehouse warehouse) {
    if (null == warehouse) {
      throw new InvalidInputException("Warehouse object can not be 'null'.");
    } else {
      DbWarehouse warehouseEntity = findActiveEntityByBusinessUnitCode(warehouse.businessUnitCode);
      if (null != warehouseEntity) {
        delete(warehouseEntity);
      } else {
        throw new InvalidInputException("Can not remove as Warehouse with Business Unit Code " + warehouse.businessUnitCode + " is not present.");
      }
    }
  }

  @Override
  public Warehouse findByBusinessUnitCode(String buCode) {
    DbWarehouse dbWarehouseEntity = findEntityByBusinessUnitCode(buCode);
    if (null != dbWarehouseEntity) {
      Warehouse warehouseObj = new Warehouse();
      warehouseObj.businessUnitCode = dbWarehouseEntity.businessUnitCode;
      warehouseObj.location = dbWarehouseEntity.location;
      warehouseObj.capacity = dbWarehouseEntity.capacity;
      warehouseObj.stock = dbWarehouseEntity.stock;
      warehouseObj.createdAt = dbWarehouseEntity.createdAt;
      warehouseObj.archivedAt = dbWarehouseEntity.archivedAt;

      return warehouseObj;
    } else {
      return null;
    }
  }

  public DbWarehouse findByWarehouseId(Long id) {
    return find("id", id).firstResult();
  }

  public DbWarehouse findEntityByBusinessUnitCode(String businessUnitCode) {
    return find("businessUnitCode", businessUnitCode).firstResult();
  }

  public List<DbWarehouse> findEntityListByBusinessUnitCode(String businessUnitCode) {
    return find("businessUnitCode", businessUnitCode).list();
  }

  public DbWarehouse findActiveEntityByBusinessUnitCode(String businessUnitCode) {
    List<DbWarehouse> entityListByBusinessUnitCode = findEntityListByBusinessUnitCode(businessUnitCode);
    if (entityListByBusinessUnitCode.size() > 0) {
      return entityListByBusinessUnitCode.stream().filter(dbWarehouse -> null == dbWarehouse.archivedAt).findFirst().orElse(null);
    } else {
      return null;
    }
  }

  public DbWarehouse createWareHouseEntityFromObj(Warehouse warehouse) {
    DbWarehouse dbWarehouse = new DbWarehouse();
    dbWarehouse.businessUnitCode = warehouse.businessUnitCode;
    dbWarehouse.location = warehouse.location;
    dbWarehouse.capacity = warehouse.capacity;
    dbWarehouse.stock = warehouse.stock;
    dbWarehouse.createdAt = getCurrentTime();
    dbWarehouse.archivedAt = warehouse.archivedAt;

    return dbWarehouse;
  }

  public DbWarehouse createWarehouse(String businessUnitCode, String location, Integer capacity, Integer stock, LocalDateTime archivedAt) {
    DbWarehouse warehouseEntity = new DbWarehouse();
    warehouseEntity.businessUnitCode = businessUnitCode;
    warehouseEntity.location = location;
    warehouseEntity.capacity = capacity;
    warehouseEntity.stock = stock;
    warehouseEntity.createdAt = getCurrentTime();
    warehouseEntity.archivedAt = archivedAt;
    persist(warehouseEntity);
    return warehouseEntity;
  }

  public LocalDateTime getCurrentTime() {
    return LocalDateTime.now();
  }

  public List<DbWarehouse> getWarehouseListByLocation(String locationIdentifier) {
    return find("location", locationIdentifier).list();
  }

  public List<DbWarehouse> getActiveWarehouseListByLocation(String locationIdentifier) {
    return getWarehouseListByLocation(locationIdentifier).stream().filter(dbWarehouse -> null == dbWarehouse.archivedAt).collect(Collectors.toList());
  }
}
