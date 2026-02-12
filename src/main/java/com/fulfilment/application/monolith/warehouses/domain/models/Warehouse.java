package com.fulfilment.application.monolith.warehouses.domain.models;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class Warehouse {

  // unique identifier
  public String businessUnitCode;

  public String location;

  public Integer capacity;

  public Integer stock;

  public LocalDateTime createdAt;

  public LocalDateTime archivedAt;

  public Warehouse(String businessUnitCode, String location,
                   Integer capacity, Integer stock, LocalDateTime createdAt,
                   LocalDateTime archivedAt) {
    this.businessUnitCode = businessUnitCode;
    this.location = location;
    this.capacity = capacity;
    this.stock = stock;
    this.createdAt = createdAt;
    this.archivedAt = archivedAt;
  }
}
