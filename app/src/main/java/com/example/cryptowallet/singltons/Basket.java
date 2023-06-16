package com.example.cryptowallet.singltons;

import com.example.cryptowallet.adapters.Item;

public class Basket {
    private static Basket instance;
    private Item item;

    private Basket() {
        // Private constructor to prevent instantiation
    }

    public static Basket getInstance() {
        if (instance == null) {
            instance = new Basket();
        }
        return instance;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

}
