package com.example.datarestwarehouse.projection;

import com.example.datarestwarehouse.entity.Currency;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = Currency.class)
public interface CustomCurrency {

    Integer getId();

    String getName();

    boolean isActive();
}
