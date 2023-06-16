package com.example.cryptowallet.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptowallet.API.CryptoCompareApi;
import com.example.cryptowallet.API.PriceResponse;
import com.example.cryptowallet.MyApplication;
import com.example.cryptowallet.R;
import com.example.cryptowallet.singltons.Basket;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {

    private List<Item> itemList;

    public CatalogAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.catalog_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        ImageView image = holder.itemImage;
        TextView itemName = holder.itemName;
        TextView itemPrice = holder.itemPrice;
        TextView itemCount = holder.itemCount;
        ImageView storeButton = holder.storeButton;
        Basket basket = Basket.getInstance();
        image.setImageResource(item.getImageResId());
        itemName.setText(item.getName());
        itemCount.setText("Кількість: " + item.getCount());
        itemPrice.setText("Ціна: " + item.getPrice());
        //fetchCryptoPrice(item.getCode(), itemCount);

        /* holder.likeButton.setOnClickListener(v -> {
            // Handle heart button click
        });
*/
        storeButton.setOnClickListener(v -> {
            basket.setItem(item);
            Toast.makeText(v.getContext(), item.getName() + " added to basket",  Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName;
        TextView itemCount;
        TextView itemPrice;
        ImageView heartButton;
        ImageView storeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemCount = itemView.findViewById(R.id.itemCount);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            storeButton = itemView.findViewById(R.id.storeButton);
        }
    }


}


