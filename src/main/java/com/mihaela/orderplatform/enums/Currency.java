package com.mihaela.orderplatform.enums;

import java.util.Arrays;

public enum Currency {

    EUR,
    USD,
    GBP,
    JPY,
    CNY,
    CHF,
    CAD,
    AUD,
    INR,
    KRW,
    BRL,
    RUB,
    MXN,
    SGD,
    HKD,
    NZD,
    SEK,
    NOK,
    DKK,
    BGN,
    UNKNOWN;


    public static Currency fromValue(String value) {
        if (value == null || value.isBlank()) {
            return UNKNOWN;
        }

        return Arrays.stream(values())
                .filter(currency -> currency.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(UNKNOWN);
    }
}