package com.fulfilment.application.monolith.warehouses.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class Warehouse {

  // unique identifier
  public Integer id;
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
