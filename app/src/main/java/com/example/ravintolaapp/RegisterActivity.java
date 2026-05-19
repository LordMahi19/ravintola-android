package com.example.ravintolaapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ravintolaapp.models.RegisterRequest;
import com.example.ravintolaapp.models.RegisterResponse;
import com.example.ravintolaapp.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnRegister;
    private TextView tvLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnRegister = findViewById(R.id.btn_register);
        tvLoginLink = findViewById(R.id.tv_login_link);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();
            }
        });

        tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Back to Login
            }
        });
    }

    private void performRegistration() {
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

        btnRegister.setEnabled(false);
        btnRegister.setText("Creating Account...");

        RegisterRequest request = new RegisterRequest(username, password);
        ApiClient.getApiService(this).register(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                btnRegister.setEnabled(true);
                btnRegister.setText(R.string.btn_register);

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "Account created successfully! Please Sign In.", Toast.LENGTH_LONG).show();
                    finish(); // Navigate back to LoginActivity
                } else {
                    Toast.makeText(RegisterActivity.this, "Username already exists or invalid password style", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                btnRegister.setEnabled(true);
                btnRegister.setText(R.string.btn_register);
                Toast.makeText(RegisterActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
