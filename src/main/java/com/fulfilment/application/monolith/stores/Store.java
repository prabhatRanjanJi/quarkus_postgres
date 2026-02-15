package com.fulfilment.application.monolith.stores;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
@Cacheable
public class Store extends PanacheEntity {

  public Long id;

  @Column(length = 40, unique = true)
  public String name;

  public int quantityProductsInStock;

  public Store() {}

  public Store(String name) {
    this.name = name;
  }

  public Store(Long id, String name, int quantityProductsInStock) {
    this.id = id;
    this.name = name;
    this.quantityProductsInStock = quantityProductsInStock;
  }
}
