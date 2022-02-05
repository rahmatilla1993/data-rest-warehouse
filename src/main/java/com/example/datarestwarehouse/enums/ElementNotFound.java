package com.example.datarestwarehouse.enums;

public enum ElementNotFound {

    INPUT("Kirim topilmadi"),
    WAREHOUSE("Ombor topilmadi"),
    SUPPLIER("Ta'minotchi topilmadi"),
    CURRENCY("Pul birligi topilmadi"),
    CATEGORY("Categoriya topilmadi"),
    PRODUCT("Product topilmadi"),
    ATTACHMENT("Fayl topilmadi"),
    MEASUREMENT("Bunday o'lchov birligi yo'q"),
    USER("Foydalanuvchi topilmadi"),
    INPUT_PRODUCT("Kirimda kirgan mahsulot omborda mavjudmas");

    private final String message;

    ElementNotFound(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
