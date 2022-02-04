package com.example.datarestwarehouse.projection;

import com.example.datarestwarehouse.entity.Measurement;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = Measurement.class)
public interface CustomMeasurement {

    Integer getId();

    String getName();

    boolean isActive();
}
