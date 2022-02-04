package com.example.datarestwarehouse.enums;

public enum ElementIsActive {

    WAREHOUSE("Ombor active holatdamas"),
    SUPPLIER("Ta'minotchi active holatdamas"),
    CURRENCY("Pul birligi active holatdamas"),
    PRODUCT("Product active holatdamas"),
    MEASUREMENT("O'lchov birligi active holatdamas");

    private final String messageActive;

    ElementIsActive(String messageActive) {
        this.messageActive = messageActive;
    }

    public String getMessageActive() {
        return messageActive;
    }
}
