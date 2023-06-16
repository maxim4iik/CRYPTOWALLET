package com.example.cryptowallet.API;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CryptoCompareApi {
    @GET("price")
    Call<JsonObject> getPrice(
            @Query("fsym") String symbol,
            @Query("tsyms") String currency,
            @Query("api_key") String apiKey
    );

    @GET("v2/histominute")
    Call<JsonObject> getHistoricalMinutePrice(
            @Query("fsym") String symbol,
            @Query("tsym") String currency,
            @Query("limit") int limit,
            @Query("api_key") String apiKey
    );

    @GET("v2/histohour")
    Call<JsonObject> getHistoricalHourPrice(
            @Query("fsym") String symbol,
            @Query("tsym") String currency,
            @Query("limit") int limit,
            @Query("api_key") String apiKey
    );

    @GET("v2/histoday")
    Call<JsonObject> getHistoricalDayPrice(
            @Query("fsym") String symbol,
            @Query("tsym") String currency,
            @Query("limit") int limit,
            @Query("api_key") String apiKey
    );
}

