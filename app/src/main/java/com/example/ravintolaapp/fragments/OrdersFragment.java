package com.example.ravintolaapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravintolaapp.LoginActivity;
import com.example.ravintolaapp.OrderDetailActivity;
import com.example.ravintolaapp.R;
import com.example.ravintolaapp.adapters.OrderAdapter;
import com.example.ravintolaapp.models.Order;
import com.example.ravintolaapp.network.ApiClient;
import com.example.ravintolaapp.network.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment {

    private RecyclerView rvOrders;
    private LinearLayout layoutAuthPrompt, layoutEmptyState;
    private Button btnLoginPrompt;

    private List<Order> orderList = new ArrayList<>();
    private OrderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        rvOrders = view.findViewById(R.id.rv_orders);
        layoutAuthPrompt = view.findViewById(R.id.layout_orders_auth_prompt);
        layoutEmptyState = view.findViewById(R.id.layout_orders_empty);
        btnLoginPrompt = view.findViewById(R.id.btn_orders_login);

        btnLoginPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        checkSessionState();

        return view;
    }

    private void checkSessionState() {
        boolean loggedIn = TokenManager.getInstance(getContext()).isLoggedIn();
        
        if (!loggedIn) {
            rvOrders.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.GONE);
            layoutAuthPrompt.setVisibility(View.VISIBLE);
        } else {
            layoutAuthPrompt.setVisibility(View.GONE);
            fetchOrdersHistory();
        }
    }

    private void fetchOrdersHistory() {
        ApiClient.getApiService(getContext()).getMyOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderList = response.body();
                    setupRecyclerView();
                    
                    if (orderList.isEmpty()) {
                        rvOrders.setVisibility(View.GONE);
                        layoutEmptyState.setVisibility(View.VISIBLE);
                    } else {
                        rvOrders.setVisibility(View.VISIBLE);
                        layoutEmptyState.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new OrderAdapter(orderList, order -> {
            Intent intent = new Intent(getContext(), OrderDetailActivity.class);
            intent.putExtra("order_id", order.getId());
            startActivity(intent);
        });
        rvOrders.setAdapter(adapter);
    }
}
