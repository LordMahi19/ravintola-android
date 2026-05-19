package com.example.ravintolaapp;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravintolaapp.adapters.OrderItemAdapter;
import com.example.ravintolaapp.models.Order;
import com.example.ravintolaapp.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvTitle, tvStatus, tvDate, tvTotal;
    private CardView cardStatusBadge;
    private RecyclerView rvItems;

    private int orderId;
    private OrderItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        btnBack = findViewById(R.id.btn_order_detail_back);
        tvTitle = findViewById(R.id.tv_detail_order_title);
        tvStatus = findViewById(R.id.tv_detail_status);
        tvDate = findViewById(R.id.tv_detail_date);
        tvTotal = findViewById(R.id.tv_detail_total);
        cardStatusBadge = findViewById(R.id.card_detail_status_badge);
        rvItems = findViewById(R.id.rv_order_detail_items);

        orderId = getIntent().getIntExtra("order_id", -1);

        btnBack.setOnClickListener(v -> finish());

        if (orderId != -1) {
            tvTitle.setText("Order #" + orderId);
            fetchOrderDetail();
        } else {
            Toast.makeText(this, "Invalid Order ID", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fetchOrderDetail() {
        ApiClient.getApiService(this).getOrderById(orderId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Order order = response.body();
                    bindOrderDetails(order);
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Failed to load order details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindOrderDetails(Order order) {
        tvTotal.setText(String.format("$%.2f", order.getTotalPrice()));
        
        String dateStr = order.getCreatedAt();
        if (dateStr != null && dateStr.length() > 10) {
            dateStr = dateStr.replace("T", " ").substring(0, 16);
        }
        tvDate.setText(dateStr != null ? dateStr : "Just now");

        String status = order.getStatus() != null ? order.getStatus().toLowerCase() : "pending";
        tvStatus.setText(status);

        // Style status badge
        int badgeColor;
        switch (status) {
            case "processing":
                badgeColor = getResources().getColor(R.color.status_processing);
                break;
            case "completed":
                badgeColor = getResources().getColor(R.color.status_completed);
                break;
            case "pending":
            default:
                badgeColor = getResources().getColor(R.color.status_pending);
                break;
        }
        cardStatusBadge.setCardBackgroundColor(badgeColor);

        // Bind items
        if (order.getItems() != null) {
            adapter = new OrderItemAdapter(order.getItems());
            rvItems.setAdapter(adapter);
        }
    }
}
