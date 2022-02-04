package com.example.datarestwarehouse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputDTO {

    @NotNull(message = "Date kiritilmadi")
    private Date date;

    @NotNull(message = "warehouse_id kiritilmadi")
    private Integer warehouse_id;

    @NotNull(message = "supplier_id kiritilmadi")
    private Integer supplier_id;

    @NotNull(message = "currency_id kiritilmadi")
    private Integer currency_id;

    @NotNull(message = "factureNumber kiritilmadi")
    private String factureNumber;

    @NotNull(message = "code kiritilmadi")
    private String code;
}
