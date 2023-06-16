package com.example.cryptowallet.API;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class PriceResponse {

    @SerializedName("USD")
    @Expose
    private Double usd;

    public Double getUsd() {
        return usd;
    }

    public void setUsd(Double usd) {
        this.usd = usd;
    }

}

