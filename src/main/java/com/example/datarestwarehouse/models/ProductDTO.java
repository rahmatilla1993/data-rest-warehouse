package com.example.datarestwarehouse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    @NotNull(message = "name kiritilmadi")
    private String name;

    private boolean active = true;

    private String code;

    @NotNull(message = "category_id kiritilmadi")
    private Integer category_id;

    @NotNull(message = "measurement_id kiritilmadi")
    private Integer measurement_id;

    @NotNull(message = "attachment_id kiritilmadi")
    private Integer attachment_id;
}
