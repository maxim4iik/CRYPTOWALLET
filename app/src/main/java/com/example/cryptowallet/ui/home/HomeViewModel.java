package com.example.cryptowallet.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cryptowallet.R;
import com.example.cryptowallet.adapters.Item;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<List<Item>> itemListLiveData = new MutableLiveData<>();
    private List<Item> itemList;

    public HomeViewModel() {
        itemList = new ArrayList<>();
        // Add initial items to the list
        itemList.add(new Item(R.drawable.img, "Bitcoin", "100", "..."));
        itemList.add(new Item(R.drawable.etherium, "Ethereum", "1000", "..."));
        itemList.add(new Item(R.drawable.ripple, "Ripple", "10000", "..."));
        itemListLiveData.setValue(itemList);
    }

    public LiveData<List<Item>> getItemList() {
        return itemListLiveData;
    }

    public void updateItemPrice(String name, String newPrice) {
        List<Item> itemList = itemListLiveData.getValue();
        if (itemList != null) {
            for (Item item : itemList) {
                if (item.getName().equals(name)) {
                    item.setPrice(newPrice);
                    break;
                }
            }
            itemListLiveData.setValue(itemList);
        }
    }

    public void updateItemCount(String name, String newCount) {
        List<Item> itemList = itemListLiveData.getValue();
        if (itemList != null) {
            for (Item item : itemList) {
                if (item.getName().equals(name)) {
                    item.setCount(newCount);
                    break;
                }
            }
            itemListLiveData.setValue(itemList);
        }
    }
}