package com.example.datarestwarehouse.projection;

import com.example.datarestwarehouse.entity.Warehouse;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = Warehouse.class)
public interface CustomWarehouse {

    Integer getId();

    String getName();

    boolean isActive();
}
