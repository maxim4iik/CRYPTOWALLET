package com.example.cryptowallet.API;

import com.google.gson.annotations.SerializedName;

public class HistoricalPriceData {
    @SerializedName("open")
    private double open;

    @SerializedName("high")
    private double high;

    @SerializedName("low")
    private double low;

    @SerializedName("close")
    private double close;

    // Getters and setters

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }
}
