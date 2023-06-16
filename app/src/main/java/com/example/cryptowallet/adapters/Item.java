package com.example.cryptowallet.adapters;

public class Item {
    private int imageResId;
    private String name;
    private String count;
    private String price;

    public Item(int imageResId, String name, String count, String price) {
        this.imageResId = imageResId;
        this.name = name;
        this.count = count;
        this.price = price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }

    public String getCount() {
        return count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCount(String newCount) {
        this.count = newCount;
    }
}
