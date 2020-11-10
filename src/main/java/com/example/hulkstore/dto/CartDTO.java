package com.example.hulkstore.dto;

import java.util.Map;

public class CartDTO {
    private Map<Integer, Integer> items;

    public CartDTO() {}

    public Map<Integer, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Integer, Integer> items) {
        this.items = items;
    }
}