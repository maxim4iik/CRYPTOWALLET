package com.example.cryptowallet.ui.start;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.cryptowallet.R;
import com.example.cryptowallet.databinding.FragmentStartBinding;


public class StartFragment extends Fragment {
    private FragmentStartBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(inflater);
        binding.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(
                        requireActivity(),
                        R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_startFragment_to_loginFragment);
            }
        });
        return binding.getRoot();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }



}