package com.example.cryptowallet.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cryptowallet.API.CryptoCompareApi;
import com.example.cryptowallet.MyApplication;
import com.example.cryptowallet.R;
import com.example.cryptowallet.adapters.CatalogAdapter;
import com.example.cryptowallet.adapters.Item;
import com.example.cryptowallet.databinding.FragmentHomeBinding;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private Handler handler = new Handler();
    List<Item> itemList = new ArrayList<>();

    private HomeViewModel homeViewModel;

    private Runnable updatePricesRunnable = new Runnable() {
        @Override
        public void run() {
            fetchCryptoPrice("BTC", "Bitcoin");
            fetchCryptoPrice("ETH", "Ethereum");
            fetchCryptoPrice("XRP", "Ripple");
            adapter.notifyDataSetChanged();

            handler.postDelayed(this, 5000); // Repeat every 5 seconds
        }
    };
    private FragmentHomeBinding binding;
    private CatalogAdapter adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel.getItemList().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> itemList) {
                // Update UI with the new item list
                adapter.notifyDataSetChanged();
            }
        });

        handler.postDelayed(updatePricesRunnable, 5000);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CatalogAdapter(homeViewModel.getItemList().getValue());
        binding.recyclerView.setAdapter(adapter);
        fetchCryptoPrice("BTC", "Bitcoin");
        fetchCryptoPrice("ETH", "Ethereum");
        fetchCryptoPrice("XRP", "Ripple");
        adapter.notifyDataSetChanged();
        return binding.getRoot();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updatePricesRunnable);

    }

    private void fetchCryptoPrice(String symbol, String name) {
        CryptoCompareApi cryptoCompareApi = MyApplication.getCryptoCompareApi();
        String apiKey = "314ec75597aca4f95c33ccdfc88d5a7493f0b97d89d1cc995745ca4e89f9a32f";
        Call<JsonObject> call = cryptoCompareApi.getPrice(symbol, "USD", apiKey);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    if (jsonObject != null) {
                        homeViewModel.updateItemPrice(name, jsonObject.get("USD").getAsDouble()+"$");
                    }
                } else {
                    // Handle error response
                    // ...
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Handle network or API call failure
                // ...
            }
        });
    }



}