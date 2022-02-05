package com.example.datarestwarehouse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputProductDTO {

    @NotNull(message = "product_id kiritilmadi")
    private Integer product_id;

    @NotNull(message = "Product soni kiritilmadi")
    private Integer amount;

    @NotNull(message = "Product narxi kiritilmadi")
    private Double price;

    @NotNull(message = "Productni yaroqlik muddati kiritilmadi")
    private Date expireDate;

    @NotNull(message = "Kirim kiritilmadi")
    private Integer input_id;
}
