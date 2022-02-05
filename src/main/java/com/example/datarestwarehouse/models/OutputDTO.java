package com.example.datarestwarehouse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputDTO {

    @NotNull(message = "Chiqim sanasi kiritilmadi")
    private Date date;

    @NotNull(message = "Ombor id si kiritilmadi")
    private Integer warehouse_id;

    @NotNull(message = "Pul birligi id si kiritilmadi")
    private Integer currency_id;

    @NotNull(message = "Mijoz id si kiritilmadi")
    private Integer client_id;

    @NotNull(message = "Factura raqami kiritilmadi")
    private String factureNumber;

    private String code;
}
