package com.fulfilment.application.monolith.warehouses.domain;

import com.fulfilment.application.monolith.exception.InvalidInputException;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang.StringUtils;

import java.util.List;

@ApplicationScoped
public class WarehouseValidator {

    private WarehouseStore warehouseStore;

    private LocationResolver locationResolver;

    public WarehouseValidator(LocationResolver locationResolver, WarehouseStore warehouseStore) {
        this.locationResolver = locationResolver;
        this.warehouseStore = warehouseStore;
    }

    public DbWarehouse validateWarehouse(Warehouse warehouse) {
        String businessUnitCode = warehouse.businessUnitCode;

        // First: validate already existing entity by businessUnitCode
        DbWarehouse dbWarehouseEntity = warehouseStore.findActiveEntityByBusinessUnitCode(businessUnitCode);
        if (null != dbWarehouseEntity) {
            throw new InvalidInputException("Warehouse with BusinessUnitCode " + businessUnitCode + " already exists.");
        }

        // Second: validate Location is present or not else thor exception
        Location location = locationResolver.resolveByIdentifier(warehouse.location);
        int maxNumberOfWarehousesAllowed = location.maxNumberOfWarehouses;

        // Third: Validate Max number of Warehouse allowed
        List<DbWarehouse> activeWarehouseList = warehouseStore.getActiveWarehouseListByLocation(warehouse.location);
        int currentWarehouseInLocation = activeWarehouseList.size();
        if (currentWarehouseInLocation < maxNumberOfWarehousesAllowed) {
            //Fourth: validate max capacity exceeded or not
            if (warehouse.capacity < location.maxCapacity) {
                // Fifth: Stock should be less than capacity
                if (warehouse.stock > warehouse.capacity) {
                    throw new InvalidInputException("Stock in a warehouse can not be more than capacity.");
                }
            } else {
                throw new InvalidInputException("Capacity in " + location.identification + " can not be more than " + location.maxCapacity+ ".");
            }
        } else {
            throw new InvalidInputException("Warehouse in " + location.identification + " can not be more than " + maxNumberOfWarehousesAllowed+ ".");
        }
        return dbWarehouseEntity;
    }

    public void validateWarehouseByBuCode(String businessUnitCode) {
        if (StringUtils.isBlank(businessUnitCode)) {
            throw new InvalidInputException("BusinessUnitCode can not be blank");
        }
        DbWarehouse dbWarehouse = warehouseStore.findActiveEntityByBusinessUnitCode(businessUnitCode);
        if (null != dbWarehouse) {
            throw new InvalidInputException("Warehouse with BusinessUnitCode " + businessUnitCode + " already exists.");
        }
    }

    public DbWarehouse validateArchive(String businessUnitCode) {
        DbWarehouse dbWarehouse = warehouseStore.findActiveEntityByBusinessUnitCode(businessUnitCode);
        if (null == dbWarehouse) {
            throw new RuntimeException("Active Warehouse with BusinessUnitCode " + businessUnitCode + " does not exist.");
        }
        if (null != dbWarehouse.archivedAt) {
            throw new RuntimeException("Warehouse with BusinessUnitCode " + businessUnitCode + " is already archived.");
        }
        return dbWarehouse;
    }

    public DbWarehouse validateArchiveById(String id) {
        DbWarehouse dbWarehouse = warehouseStore.findByWarehouseId(Long.valueOf(id));
        if (null == dbWarehouse) {
            throw new RuntimeException("Warehouse with identifier " + id + " does not exist.");
        }
        if (null != dbWarehouse.archivedAt) {
            throw new RuntimeException("Warehouse with identifier " + id + " already archived.");
        }
        return dbWarehouse;
    }

    public DbWarehouse validateWarehouseForReplacement(Warehouse newWarehouse) {
        // validate
        DbWarehouse currentWarehouse = validateWarehouse(newWarehouse);

        // additional two checks for replace
        if (newWarehouse.capacity < currentWarehouse.stock) {
            throw new InvalidInputException("Capacity can not be less than Stock.");
        }

        if (newWarehouse.stock != currentWarehouse.stock) {
            throw new InvalidInputException("Stock of the new warehouse should match the stock of the previous warehouse.");
        }

        return currentWarehouse;
    }

}
