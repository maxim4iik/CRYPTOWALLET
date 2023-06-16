package com.example.cryptowallet;

import android.app.Application;

import com.example.cryptowallet.API.CryptoCompareApi;
import com.google.firebase.FirebaseApp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {
    private static CryptoCompareApi cryptoCompareApi;

    public static CryptoCompareApi getCryptoCompareApi() {
        return cryptoCompareApi;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://min-api.cryptocompare.com/data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create API service
        cryptoCompareApi = retrofit.create(CryptoCompareApi.class);
        FirebaseApp.initializeApp(this);
    }
}