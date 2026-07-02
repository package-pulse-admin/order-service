package com.mihaela.orderplatform.domain;

public record CompositeKey(Long id, String value) {

    private static final String SEPARATOR = ":";

    public String asString() {
        return id + SEPARATOR + value;
    }

    public static CompositeKey fromString(String input) {

        String[] parts = input.split(SEPARATOR, 2);

        return new CompositeKey(
                Long.valueOf(parts[0]),
                parts[1]
        );
    }
}
