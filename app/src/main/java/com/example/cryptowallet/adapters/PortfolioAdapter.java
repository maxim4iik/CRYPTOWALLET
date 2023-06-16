package com.example.cryptowallet.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptowallet.R;
import com.example.cryptowallet.ui.start.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;


public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.ViewHolder> {

    private List<Item> itemList;
    AlertDialog.Builder builder;
    AlertDialog.Builder builder1;
    AlertDialog.Builder builderConf;
    DatabaseReference database;
    DatabaseReference userRef;
    Float minusBody;
    String userID;
    User user;
    View popupView;
    public PortfolioAdapter(List<Item> itemList, AlertDialog.Builder builder, String userID, View popupView, AlertDialog.Builder builder1, AlertDialog.Builder builderConf) {
        this.itemList = itemList;
        this.builder = builder;
        this.builder1 = builder1;
        this.userID = userID;
        this.popupView = popupView;
        this.builderConf = builderConf;
        database = FirebaseDatabase.getInstance().getReference();
        userRef = database.child("users").child(userID);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.portfolio_item, parent, false);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Item item = itemList.get(position);
        ImageView image = holder.itemImage;
        TextView itemName = holder.itemName;
        TextView itemPrice = holder.itemPrice;
        TextView itemCount = holder.itemCount;
        Button sellButton = holder.sellButton;
        Button contractButton = holder.contractButton;

        image.setImageResource(item.getImageResId());
        itemName.setText(item.getName());
        itemCount.setText("На вашому балансі: " + item.getCount());
        itemPrice.setText("Ціна: " + item.getPrice());




        contractButton.setOnClickListener( v -> {
            ViewGroup parentView = (ViewGroup) popupView.getParent();
            if (parentView != null) {
                parentView.removeView(popupView);
            }

            builder1.setPositiveButton("Вивести", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TextView textView = popupView.findViewById(R.id.editTextAmount);
                    minusBody = Float.parseFloat(textView.getText().toString());
                    if (minusBody > Float.parseFloat(item.getCount())) {
                        minusBody = 0f;
                        return;
                    };
                    builderConf.setPositiveButton("Так", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            User newUser = new User(user);
                            newUser.doContract(itemName.getText().toString(), minusBody);
                            Log.d("dsrfs", itemName.getText().toString());
                            Log.d("dsrfs", minusBody.toString());

                            userRef.setValue(newUser);
                        }
                    });
                    builderConf.create().show();
                }
            });
            builder1.create().show();
        });
        sellButton.setOnClickListener(v -> {
            ViewGroup parentView = (ViewGroup) popupView.getParent();
            if (parentView != null) {
                parentView.removeView(popupView);
            }

            builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ViewGroup parentView = (ViewGroup) v.getParent();
                    if (parentView != null) {
                        parentView.removeView(popupView);
                    }
                    User newUser = new User(user);
                    float available = 0;
                    Log.d("nma", item.getName());
                    if (Objects.equals(item.getName(), "Bitcoin")) {
                        available = user.getBtc();
                    }
                    if (Objects.equals(item.getName(), "Ethereum")) {
                        available = user.getEth();
                    }
                    if (Objects.equals(item.getName(), "Ripple")) {
                        available = user.getTrp();
                    }

                    TextView textView = popupView.findViewById(R.id.editTextAmount);
                    if (textView.getText().toString().isEmpty()) return;
                    float amountToSell = Float.parseFloat(textView.getText().toString());
                    float purchasePrice = Float.parseFloat(item.getPrice().substring(0, item.getPrice().length()-1)) * amountToSell;
                    if (available >= Float.parseFloat(item.getCount()))
                    {
                        newUser.addToBalance(purchasePrice);
                        newUser.setPortfolio(item.getName(), -1 * amountToSell);
                        userRef.setValue(newUser);

                    }
                }
            });
            builder.create().show();
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
        Button sellButton;
        Button contractButton;
        EditText countToSell;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemCount = itemView.findViewById(R.id.itemCount);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            sellButton = itemView.findViewById(R.id.sold_button);
            countToSell = itemView.findViewById(R.id.purchase_count);
            contractButton = itemView.findViewById(R.id.contract_button);
        }
    }


}


