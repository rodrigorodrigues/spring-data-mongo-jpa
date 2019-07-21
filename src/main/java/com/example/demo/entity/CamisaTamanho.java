package com.example.demo.entity;

import static java.util.Arrays.stream;

public enum CamisaTamanho {
    S(1), M(2), L(3);

    private int id;

    CamisaTamanho(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static CamisaTamanho valueOf(int id) {
        return stream(CamisaTamanho.values())
            .filter(j -> j.getId() == id)
            .findFirst()
            .orElseThrow(() -> new RuntimeException(String.format("Not found Jersey with id: %d", id)));
    }
}
