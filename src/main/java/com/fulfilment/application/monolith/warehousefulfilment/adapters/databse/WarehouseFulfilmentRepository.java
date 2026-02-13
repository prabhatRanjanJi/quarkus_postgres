package com.fulfilment.application.monolith.warehousefulfilment.adapters.databse;

import com.fulfilment.application.monolith.warehousefulfilment.domain.ports.WarehouseFulfilmentStore;
import com.fulfilment.application.monolith.warehousefulfilment.domain.model.WarehouseFulfilment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jdk.jfr.Description;

import java.time.LocalDateTime;

@ApplicationScoped
public class WarehouseFulfilmentRepository implements WarehouseFulfilmentStore, PanacheRepository<DbWarehouseFulfilment> {

    @Override
    @Description("Validation: Each `Product` can be fulfilled by a maximum of 2 different `Warehouses` per `Store`")
    public Long validateProduct(WarehouseFulfilment fulfilment) {
        return find(" select product from warehouse_fulfilment "
                + " where product = ?1 and store = ?2 "
                + " group by store, product having count(product) >= 2", fulfilment.getProduct(), fulfilment.getStore())
                .count();
    }

    @Override
    @Description("Validation: Each `Store` can be fulfilled by a maximum of 3 different `Warehouses`")
    public Long validateStore(WarehouseFulfilment fulfilment) {
        return find("select store from warehouse_fulfilment "
                + " where store = ?1 group by store having count(distinct warehouse) >= 3", fulfilment.getStore()).count();
    }

    @Override
    @Description("Validation: Each `Warehouse` can store maximally 5 types of `Products`")
    public Long validateWarehouse(WarehouseFulfilment fulfilment) {
        return find("select count(distinct product) from warehouse_fulfilment "
                + " where warehouse = ?1 "
                + " having count(distinct product) >= 5", fulfilment.getWarehouse()).count();
    }

    public void saveWarehouseFulfilment(WarehouseFulfilment warehouseFulfilment) {
        persistAndFlush(entityFromWarehouseFulfilment(warehouseFulfilment));
    }

    private DbWarehouseFulfilment entityFromWarehouseFulfilment(WarehouseFulfilment warehouseFulfilment) {
        DbWarehouseFulfilment entity = new DbWarehouseFulfilment();
        entity.setWarehouse(warehouseFulfilment.getWarehouse());
        entity.setStore(warehouseFulfilment.getStore());
        entity.setProduct(warehouseFulfilment.getProduct());
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }
}
