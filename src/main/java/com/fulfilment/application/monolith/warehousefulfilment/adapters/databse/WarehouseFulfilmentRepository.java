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
        long count = count("product = ?1 and store = ?2",
                fulfilment.getProduct(),
                fulfilment.getStore());

        return count >= 2 ? 1L : 0L;
    }




    @Override
    @Description("Validation: Each `Store` can be fulfilled by a maximum of 3 different `Warehouses`")
    public Long validateStore(WarehouseFulfilment fulfilment) {
        long distinctWarehouses = getEntityManager()
                .createQuery(
                        "select count(distinct wf.warehouse) from DbWarehouseFulfilment wf where wf.store = :store",
                        Long.class)
                .setParameter("store", fulfilment.getStore())
                .getSingleResult();

        return distinctWarehouses >= 3 ? 1L : 0L;
    }


    @Override
    @Description("Validation: Each `Warehouse` can store maximally 5 types of `Products`")
    public Long validateWarehouse(WarehouseFulfilment fulfilment) {
        long distinctProducts = getEntityManager()
                .createQuery(
                        "select count(distinct wf.product) from DbWarehouseFulfilment wf where wf.warehouse = :warehouse",
                        Long.class)
                .setParameter("warehouse", fulfilment.getWarehouse())
                .getSingleResult();

        return distinctProducts >= 5 ? 1L : 0L;
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
