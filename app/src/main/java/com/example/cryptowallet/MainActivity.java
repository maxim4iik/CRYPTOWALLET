package com.example.cryptowallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cryptowallet.adapters.Item;
import com.example.cryptowallet.databinding.ActivityMainBinding;
import com.example.cryptowallet.helpers.PopupWindowHelper;
import com.example.cryptowallet.singltons.Basket;
import com.example.cryptowallet.singltons.UserManager;
import com.example.cryptowallet.ui.start.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private View purchaseView;

    public User user;
    private UserManager userManager;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        purchaseView = LayoutInflater.from(this).inflate(R.layout.purchase_layout, null);
        navController = Navigation.findNavController(
                MainActivity.this,
                R.id.nav_host_fragment_content_main);

        binding.activityMainContent.navigationIcon.setOnClickListener(v -> {
            if (binding.customDrawer.getVisibility() == View.INVISIBLE){
                binding.customDrawer.setVisibility(View.VISIBLE);
            }
            else {
                binding.customDrawer.setVisibility(View.INVISIBLE);

            }
        });
        userManager = new UserManager(this);
        binding.activityMainContent.iconHome.setOnClickListener(v -> {
            navController.navigate(R.id.navHome);
        });
        binding.activityMainContent.iconGraph.setOnClickListener(v -> {
            navController.navigate(R.id.navChart);
        });
        binding.activityMainContent.iconPortfolio.setOnClickListener(v -> {
            navController.navigate(R.id.navPortfloio);
        });
        binding.activityMainContent.iconProfile.setOnClickListener(v -> {
            navController.navigate(R.id.navProfile);
        });

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = database.child("users").child(userManager.getUserId());
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


        PopupWindowHelper popupWindowHelper = new PopupWindowHelper(this);
        Basket basket = Basket.getInstance();
        binding.activityMainContent.basket.setOnClickListener(v -> {
            if (basket.getItem() != null) {
                Item item = basket.getItem();
                ImageView image = purchaseView.findViewById(R.id.itemImage);
                TextView itemName = purchaseView.findViewById(R.id.itemName);
                TextView itemCount = purchaseView.findViewById(R.id.itemCount);
                TextView itemPrice = purchaseView.findViewById(R.id.itemPrice);
                EditText itemCountToBuy = purchaseView.findViewById(R.id.purchase_count);
                Button button = purchaseView.findViewById(R.id.purchase_button);
                image.setImageResource(item.getImageResId());
                itemName.setText(item.getName());
                itemCount.setText("Кількість: " + item.getCount());
                itemPrice.setText("Ціна: " + item.getPrice());
                button.setOnClickListener(view -> {
                    User newUser = new User(user);
                    Float countToBuy = Float.parseFloat(itemCountToBuy.getText().toString());
                    Float purchaseValue = countToBuy*Float.parseFloat(item.getPrice().substring(0, item.getPrice().length() - 1));
                    if (purchaseValue > newUser.getBalance()){
                        Toast.makeText(view.getContext(), "У вас недостатньо коштів", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        newUser.addToBalance(-1*countToBuy*Float.parseFloat(item.getPrice().substring(0, item.getPrice().length() - 1)));
                        newUser.setPortfolio(item.getName(), countToBuy);
                        userRef.setValue(newUser);
                    }

                    popupWindowHelper.dismissPopupWindow();
                });
                popupWindowHelper.showPopupWindow(purchaseView);
            }
            else{
                Toast.makeText(this, "Nothing in basket", Toast.LENGTH_SHORT).show();
            }
        });

        binding.customDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navHome) {
                    navController.navigate(R.id.navHome);
                }
                if (item.getItemId() == R.id.navCompany) {
                    navController.navigate(R.id.navCompany);
                }
                if (item.getItemId() == R.id.navMarket) {
                    navController.navigate(R.id.navHome);
                }
                binding.customDrawer.setVisibility(View.INVISIBLE);
                return true;
            }
        });
    }
}