package com.example.datarestwarehouse.models;

import com.example.datarestwarehouse.entity.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotNull(message = "Ism kiritilmadi")
    private String firstName;

    @NotNull(message = "Familiya kiritilmadi")
    private String lastName;

    @NotNull(message = "Telefon kiritilmadi")
    private String phoneNumber;

    @NotNull(message = "Kod kiritilmadi")
    private String code;

    @NotNull(message = "Parol kiritilmadi")
    private String password;

    @NotNull(message = "Ombor id lari kiritilmadi")
    Set<Integer> warehouses;

    private boolean active = true;
}
