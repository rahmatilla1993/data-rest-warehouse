package com.example.datarestwarehouse.enums;

public enum ElementNotFound {

    INPUT("Input topilmadi"),
    WAREHOUSE("Ombor topilmadi"),
    SUPPLIER("Ta'minotchi topilmadi"),
    CURRENCY("Pul birligi topilmadi"),
    CATEGORY("Categoriya topilmadi"),
    PRODUCT("Product topilmadi"),
    ATTACHMENT("Fayl topilmadi"),
    MEASUREMENT("Bunday o'lchov birligi yo'q"),
    USER("Foydalanuvchi topilmadi");

    private final String message;

    ElementNotFound(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
