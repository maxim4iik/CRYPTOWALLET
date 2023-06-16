package com.example.cryptowallet.ui.start;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cryptowallet.R;
import com.example.cryptowallet.databinding.FragmentLoginBinding;
import com.example.cryptowallet.MainActivity;
import com.example.cryptowallet.singltons.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private Intent myIntent;
    private FirebaseAuth auth;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    EditText emailEditText ;
    EditText passwordEditText;
    EditText passwordConfirmEditText;
    EditText addressEditText;
    EditText phoneEditText;
    EditText nameEditText;
    EditText surnameEditText;

    UserManager userManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater);
        myIntent = new Intent(requireActivity(), MainActivity.class);
        auth = FirebaseAuth.getInstance();
        userManager = new UserManager(requireContext());
        binding.buttonSignIn.setOnClickListener(v -> showSignInDialog());
        binding.buttonLogIn.setOnClickListener(v -> showLogInDialog());
        return binding.getRoot();
    }

    private void showSignInDialog() {
        Dialog signUpDialog = new Dialog(requireActivity());
        signUpDialog.setContentView(R.layout.dialog_sign_in);
        // Set the width of the dialog window to match the parent
        signUpDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Find and initialize UI elements
        emailEditText = signUpDialog.findViewById(R.id.editTextEmail);
        passwordEditText = signUpDialog.findViewById(R.id.editTextPassword);
        passwordConfirmEditText = signUpDialog.findViewById(R.id.editTextConfirmPassword);
        addressEditText = signUpDialog.findViewById(R.id.editTextAddress);
        phoneEditText = signUpDialog.findViewById(R.id.editTextPhone);
        nameEditText = signUpDialog.findViewById(R.id.editTextName);
        surnameEditText = signUpDialog.findViewById(R.id.editTextSurname);
        Button signUpButton = signUpDialog.findViewById(R.id.buttonSignIn);
        Button cancelButton = signUpDialog.findViewById(R.id.buttonCancel);

        signUpButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String passwordConfirm = passwordConfirmEditText.getText().toString();
            String address = addressEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String name = nameEditText.getText().toString();
            String surname = surnameEditText.getText().toString();

            if(!verifyUserData(email, password, passwordConfirm, address, phone, name, surname)) return;
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // User creation successful
                    FirebaseUser user = auth.getCurrentUser();
                    String userId = user.getUid();
                    userManager.setUserId(userId);
                    User userData = new User(email, address, phone, name, surname);
                    database.child("users").child(userId).setValue(userData);
                    requireActivity().startActivity(myIntent);
                    signUpDialog.dismiss();

                } else {
                    Exception exception = task.getException();
                    Log.d("Error", exception.toString());
                    signUpDialog.dismiss();
                    if (exception instanceof FirebaseAuthUserCollisionException) {
                        // User is already registered
                        Toast.makeText(requireContext(), "User already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        // Other registration error occurred
                        Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        cancelButton.setOnClickListener(v -> signUpDialog.dismiss());

        signUpDialog.show();
    }

    private boolean verifyUserData(String email, String password, String passwordConfirm, String address, String phone, String name, String surname) {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Invalid email format
            emailEditText.setError("Enter a valid email address");
            emailEditText.requestFocus();
            return false;
        }

        if (password.isEmpty() || password.length() < 6) {
            // Password is empty or too short
            passwordEditText.setError("Enter a password with at least 6 characters");
            passwordEditText.requestFocus();
            return false;
        }

        if (!password.equals(passwordConfirm)) {
            // Password and confirm password do not match
            passwordConfirmEditText.setError("Passwords do not match");
            passwordConfirmEditText.requestFocus();
            return false;
        }

        if (address.isEmpty()) {
            // Address is empty
            addressEditText.setError("Enter your address");
            addressEditText.requestFocus();
            return false;
        }

        if (phone.isEmpty()) {
            // Phone number is empty
            phoneEditText.setError("Enter your phone number");
            phoneEditText.requestFocus();
            return false;
        }

        if (name.isEmpty()) {
            // Name is empty
            nameEditText.setError("Enter your name");
            nameEditText.requestFocus();
            return false;
        }

        if (surname.isEmpty()) {
            // Name is empty
            surnameEditText.setError("Enter your name");
            surnameEditText.requestFocus();
            return false;
        }

        return true; // All data is valid
    }

    private void showLogInDialog() {
        Dialog logInDialog = new Dialog(requireActivity());
        logInDialog.setContentView(R.layout.dialog_log_in);
        // Set the width of the dialog window to match the parent
        logInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Find and initialize UI elements
        EditText emailEditText = logInDialog.findViewById(R.id.editTextEmail);
        EditText passwordEditText = logInDialog.findViewById(R.id.editTextPassword);
        Button logInButton = logInDialog.findViewById(R.id.buttonLogIn);
        Button cancelButton = logInDialog.findViewById(R.id.buttonCancel);

        logInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (email.isEmpty()) {
                email = "test123@test.com";
            }

            if (password.isEmpty()) {
                password = "test123";
            }
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity(), task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("sadgas", "createUserWithEmail:success");
                    FirebaseUser user = auth.getCurrentUser();
                    String userId = user.getUid();
                    userManager.setUserId(userId);

                    requireActivity().startActivity(myIntent);
                    logInDialog.dismiss();


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("sadgas", "createUserWithEmail:failure", task.getException());

                }
            });
        });

        cancelButton.setOnClickListener(v -> logInDialog.dismiss());

        logInDialog.show();
    }
}