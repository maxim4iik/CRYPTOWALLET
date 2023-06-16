package com.example.cryptowallet.ui.portfolio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.cryptowallet.API.CryptoCompareApi;
import com.example.cryptowallet.MyApplication;
import com.example.cryptowallet.R;
import com.example.cryptowallet.adapters.CatalogAdapter;
import com.example.cryptowallet.adapters.Item;
import com.example.cryptowallet.adapters.PortfolioAdapter;
import com.example.cryptowallet.databinding.FragmentPortfolioBinding;
import com.example.cryptowallet.singltons.UserManager;
import com.example.cryptowallet.ui.home.HomeViewModel;
import com.example.cryptowallet.ui.start.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PortfolioFragment extends Fragment {

    private FragmentPortfolioBinding binding;
    private PortfolioViewModel portfolioViewModel;
    private PortfolioAdapter adapter;

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPortfolioBinding.inflate(inflater);
        portfolioViewModel = new ViewModelProvider(this).get(PortfolioViewModel.class);
        portfolioViewModel.getItemList().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> itemList) {
                // Update UI with the new item list
                adapter.notifyDataSetChanged();
            }
        });
        UserManager userManager = new UserManager(requireActivity());
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = database.child("users").child(userManager.getUserId());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(User.class);
                    portfolioViewModel.updateItemCount("Bitcoin", String.valueOf(snapshot.getValue(User.class).getBtc()));
                    portfolioViewModel.updateItemCount("Ethereum", String.valueOf(snapshot.getValue(User.class).getEth()));
                    portfolioViewModel.updateItemCount("Ripple", String.valueOf(snapshot.getValue(User.class).getTrp()));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Підтвердження списання");

        View popupView = getLayoutInflater().inflate(R.layout.add_to_balance_popup, null);

        builder.setView(popupView);


        builder.setNegativeButton("Ні", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });


        AlertDialog.Builder builder2 = new AlertDialog.Builder(requireContext());
        builder2.setTitle("Виберіть суму для списання");
        builder2.setView(popupView);

        builder2.setNegativeButton("Ні", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog.Builder builder3 = new AlertDialog.Builder(requireContext());
        builder3.setTitle("Підтвердити смарт-контракт?");


        builder3.setNegativeButton("Ні", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new PortfolioAdapter(portfolioViewModel.getItemList().getValue(), builder, userManager.getUserId(), popupView, builder2, builder3);
        binding.recyclerView.setAdapter(adapter);
        fetchCryptoPrice("BTC", "Bitcoin");
        fetchCryptoPrice("ETH", "Ethereum");
        fetchCryptoPrice("XRP", "Ripple");
        adapter.notifyDataSetChanged();



        return binding.getRoot();
    }

    private void fetchCryptoPrice(String symbol, String name) {
        CryptoCompareApi cryptoCompareApi = MyApplication.getCryptoCompareApi();
        String apiKey = "";
        Call<JsonObject> call = cryptoCompareApi.getPrice(symbol, "USD", apiKey);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    if (jsonObject != null) {
                        portfolioViewModel.updateItemPrice(name, jsonObject.get("USD").getAsDouble() + "$");
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