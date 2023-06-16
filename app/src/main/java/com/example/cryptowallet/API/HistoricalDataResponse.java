package com.example.cryptowallet.API;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class HistoricalDataResponse {
    @SerializedName("Data")
    private Map<String, HistoricalPriceData> data;

    // Getters and setters

    public Map<String, HistoricalPriceData> getData() {
        return data;
    }

    public void setData(Map<String, HistoricalPriceData> data) {
        this.data = data;
    }
}
