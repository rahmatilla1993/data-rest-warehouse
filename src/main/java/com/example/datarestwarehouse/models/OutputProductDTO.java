package com.example.datarestwarehouse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputProductDTO {

    @NotNull(message = "Mahsulot kiritilmadi")
    private Integer product_id;

    @NotNull(message = "Product soni kiritilmadi")
    private Integer amount;

    @NotNull(message = "Product narxi kiritilmadi")
    private Double price;

    @NotNull(message = "Chiqim id si kiritilmadi")
    private Integer output_id;
}
