package com.example.ravintolaapp.fragments;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravintolaapp.R;
import com.example.ravintolaapp.adapters.CartAdapter;
import com.example.ravintolaapp.models.CartItem;
import com.example.ravintolaapp.models.Order;
import com.example.ravintolaapp.models.OrderRequest;
import com.example.ravintolaapp.network.ApiClient;
import com.example.ravintolaapp.utils.CartManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private RecyclerView rvCartItems;
    private LinearLayout layoutEmptyState;
    private TextView tvTotalPrice;
    private Button btnCheckout;

    private CartAdapter adapter;
    private List<CartItem> cartItemsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        rvCartItems = view.findViewById(R.id.rv_cart_items);
        layoutEmptyState = view.findViewById(R.id.layout_cart_empty);
        tvTotalPrice = view.findViewById(R.id.tv_cart_total_price);
        btnCheckout = view.findViewById(R.id.btn_cart_checkout);

        cartItemsList = CartManager.getInstance().getCartItems();

        setupRecyclerView();
        updateUI();

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrderToServer();
            }
        });

        return view;
    }

    private void setupRecyclerView() {
        adapter = new CartAdapter(cartItemsList, new CartAdapter.OnCartItemChangedListener() {
            @Override
            public void onItemDeleted(CartItem item) {
                CartManager.getInstance().removeCartItem(item);
                updateUI();
            }

            @Override
            public void onItemQtyChanged(CartItem item, int newQty) {
                CartManager.getInstance().updateQuantity(item, newQty);
                updateUI();
            }
        });
        rvCartItems.setAdapter(adapter);
    }

    private void updateUI() {
        if (cartItemsList.isEmpty()) {
            rvCartItems.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
            btnCheckout.setEnabled(false);
        } else {
            rvCartItems.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);
            btnCheckout.setEnabled(true);
        }
        
        tvTotalPrice.setText(String.format("$%.2f", CartManager.getInstance().getTotalPrice()));
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void placeOrderToServer() {
        if (cartItemsList.isEmpty()) return;

        btnCheckout.setEnabled(false);
        btnCheckout.setText("Processing...");

        // Map local cart items to OrderRequest body expected by the server
        List<OrderRequest.OrderItemRequest> requestItems = new ArrayList<>();
        for (CartItem ci : cartItemsList) {
            if (ci.isCustomPizza()) {
                requestItems.add(new OrderRequest.OrderItemRequest(ci.getCustomPizza(), ci.getQuantity()));
            } else {
                requestItems.add(new OrderRequest.OrderItemRequest(ci.getMenuItem().getId(), ci.getQuantity()));
            }
        }

        OrderRequest requestBody = new OrderRequest(requestItems);
        
        ApiClient.getApiService(getContext()).placeOrder(requestBody).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                btnCheckout.setEnabled(true);
                btnCheckout.setText(R.string.btn_place_order);

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), R.string.order_placed_success, Toast.LENGTH_LONG).show();
                    
                    // Clear local cart
                    CartManager.getInstance().clearCart();
                    updateUI();
                } else {
                    Toast.makeText(getContext(), "Failed to place order: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                btnCheckout.setEnabled(true);
                btnCheckout.setText(R.string.btn_place_order);
                Toast.makeText(getContext(), "Checkout error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
