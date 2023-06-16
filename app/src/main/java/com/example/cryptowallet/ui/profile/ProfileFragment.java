package com.example.cryptowallet.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cryptowallet.R;
import com.example.cryptowallet.adapters.Item;
import com.example.cryptowallet.databinding.FragmentProfileBinding;
import com.example.cryptowallet.singltons.UserManager;
import com.example.cryptowallet.ui.start.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private UserManager userManager;
    float minusMoney = 0;

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater);
        userManager = new UserManager(requireContext());
        Log.d("fhfd", userManager.getUserId());
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = database.child("users").child(userManager.getUserId());
        Log.d("fhfd", userRef.toString());

        View popupView = getLayoutInflater().inflate(R.layout.add_to_balance_popup, null);
        View anotherPopupView = getLayoutInflater().inflate(R.layout.add_to_balance_popup, null);

        EditText editTextAmount = popupView.findViewById(R.id.editTextAmount);
        EditText editTextAmountForMinus = anotherPopupView.findViewById(R.id.editTextAmount);


        AlertDialog.Builder builder3 = new AlertDialog.Builder(requireContext());
        builder3.setTitle("Підтвердження списання");
        builder3.setPositiveButton("Так", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User newUser = new User(user);
                newUser.addToBalance(-1 * minusMoney);
                userRef.setValue(newUser);
            }
        });

        builder3.setNegativeButton("Ні", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Підтвердити смарт-контракт?");
        builder.setView(popupView);

        builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User newUser = new User(user);
                minusMoney = Float.parseFloat(editTextAmount.getText().toString());
                if (minusMoney > newUser.getBalance()) {
                    Toast.makeText(requireContext(), "Недостатньо коштів", Toast.LENGTH_SHORT).show();
                    minusMoney = 0;
                    return;
                }
                newUser.addToBalance(-1 * minusMoney);
                userRef.setValue(newUser);
            }
        });

        builder.setNegativeButton("Ні", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });




        AlertDialog.Builder builder2 = new AlertDialog.Builder(requireContext());
        builder2.setTitle("Введіть суму поповнення");
        builder2.setView(popupView);
        builder2.setPositiveButton("Додати", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User newUser = new User(user);
                float amount = Float.parseFloat(editTextAmount.getText().toString());
                newUser.addToBalance(amount);
                userRef.setValue(newUser);
            }
        });

        builder2.setNegativeButton("Відміна", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });


        binding.minus.setOnClickListener(v -> {
            ViewGroup parentView = (ViewGroup) popupView.getParent();
            if (parentView != null) {
                parentView.removeView(popupView);
            }
            builder.setMessage("Вкажіть суму списання");
            AlertDialog dialog = builder.create();
            dialog.show();

        });


        binding.plus.setOnClickListener(v -> {
            ViewGroup parentView = (ViewGroup) popupView.getParent();
            AlertDialog dialog2 = builder2.create();
            if (parentView != null) {
                parentView.removeView(popupView);
            }
            dialog2.show();
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(User.class);
                    binding.surname.setText("Ім'я: " + user.getName());
                    binding.name.setText("Прізвище: " + user.getSurname());
                    binding.address.setText("Аддреса: " + user.getAddress());
                    binding.phone.setText("Телефон: " + user.getPhone());
                    binding.balance.setText("Баланс: " + user.getBalance() + "$");
                    binding.cryptoAddress.setText("Криптоадрес: 0x" + userManager.getUserId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
}