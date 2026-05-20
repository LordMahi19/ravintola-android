package com.example.ravintolaapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ravintolaapp.LoginActivity;
import com.example.ravintolaapp.R;
import com.example.ravintolaapp.network.TokenManager;

public class ProfileFragment extends Fragment {

    private LinearLayout layoutLoggedIn, layoutGuest;
    private TextView tvUsername, tvRole, tvJoined;
    private Button btnLogout, btnLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        layoutLoggedIn = view.findViewById(R.id.layout_profile_logged_in);
        layoutGuest = view.findViewById(R.id.layout_profile_guest);
        tvUsername = view.findViewById(R.id.tv_profile_username);
        tvRole = view.findViewById(R.id.tv_profile_role);
        tvJoined = view.findViewById(R.id.tv_profile_joined);
        btnLogout = view.findViewById(R.id.btn_profile_logout);
        btnLogin = view.findViewById(R.id.btn_profile_login);

        setupClickListeners();
        updateUI();

        return view;
    }

    private void setupClickListeners() {
        btnLogout.setOnClickListener(v -> {
            TokenManager.getInstance(getContext()).clearAuthData();
            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            
            // Redirect to Login Screen
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void updateUI() {
        TokenManager tm = TokenManager.getInstance(getContext());
        boolean loggedIn = tm.isLoggedIn();

        if (loggedIn) {
            layoutGuest.setVisibility(View.GONE);
            layoutLoggedIn.setVisibility(View.VISIBLE);

            tvUsername.setText(tm.getUsername());
            
            String roleStr = tm.getRole();
            tvRole.setText(roleStr != null ? roleStr.toUpperCase() + " CUSTOMER" : "CUSTOMER");
            
            // Format fallback date
            tvJoined.setText("Member since 2026");
        } else {
            layoutLoggedIn.setVisibility(View.GONE);
            layoutGuest.setVisibility(View.VISIBLE);
        }
    }
}
