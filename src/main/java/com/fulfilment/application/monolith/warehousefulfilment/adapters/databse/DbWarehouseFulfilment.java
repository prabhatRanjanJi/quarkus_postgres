package com.fulfilment.application.monolith.warehousefulfilment.adapters.databse;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse_fulfilment")
@Getter
@Setter
@NoArgsConstructor
public class DbWarehouseFulfilment {

    @Id
    @GeneratedValue
    private Long id;

    private String store;
    private String product;
    private String warehouse;
    private LocalDateTime createdAt;
}
