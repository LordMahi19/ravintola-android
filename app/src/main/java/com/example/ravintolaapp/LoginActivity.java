package com.example.ravintolaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ravintolaapp.models.LoginRequest;
import com.example.ravintolaapp.models.LoginResponse;
import com.example.ravintolaapp.network.ApiClient;
import com.example.ravintolaapp.network.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnGuest;
    private TextView tvRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnGuest = findViewById(R.id.btn_guest);
        tvRegisterLink = findViewById(R.id.tv_register_link);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Continue as guest by clearing previous login data
                TokenManager.getInstance(LoginActivity.this).clearAuthData();
                navigateToMain();
            }
        });

        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void performLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty()) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Signing In...");

        LoginRequest request = new LoginRequest(username, password);
        ApiClient.getApiService(this).login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnLogin.setEnabled(true);
                btnLogin.setText(R.string.btn_login);

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    
                    // Save JWT auth token to SharedPreferences
                    TokenManager.getInstance(LoginActivity.this).saveAuthData(
                            loginResponse.getToken(),
                            loginResponse.getUser().getUsername(),
                            loginResponse.getUser().getRole(),
                            loginResponse.getUser().getId()
                    );

                    Toast.makeText(LoginActivity.this, "Welcome " + loginResponse.getUser().getUsername() + "!", Toast.LENGTH_SHORT).show();
                    navigateToMain();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnLogin.setEnabled(true);
                btnLogin.setText(R.string.btn_login);
                Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
