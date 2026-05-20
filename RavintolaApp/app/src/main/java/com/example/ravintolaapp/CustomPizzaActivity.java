package com.example.ravintolaapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravintolaapp.adapters.IngredientAdapter;
import com.example.ravintolaapp.models.CustomPizzaRequest;
import com.example.ravintolaapp.models.Ingredient;
import com.example.ravintolaapp.network.ApiClient;
import com.example.ravintolaapp.utils.CartManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomPizzaActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private CardView cardSize8, cardSize10, cardSize12;
    private RecyclerView rvToppings;
    private TextView tvTotalPrice;
    private Button btnAddCart;

    private List<Ingredient> ingredientList = new ArrayList<>();
    private IngredientAdapter adapter;

    private int selectedSize = 10; // Default Medium (10")
    private double basePrice = 10.00; // Base price for Medium
    private double currentTotalPrice = 10.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_pizza);

        btnBack = findViewById(R.id.btn_pizza_back);
        cardSize8 = findViewById(R.id.card_size_8);
        cardSize10 = findViewById(R.id.card_size_10);
        cardSize12 = findViewById(R.id.card_size_12);
        rvToppings = findViewById(R.id.rv_pizza_toppings);
        tvTotalPrice = findViewById(R.id.tv_custom_pizza_total);
        btnAddCart = findViewById(R.id.btn_pizza_add_cart);

        setupSizeSelection();
        fetchIngredients();

        btnBack.setOnClickListener(v -> finish());
        btnAddCart.setOnClickListener(v -> submitCustomPizza());
    }

    private void setupSizeSelection() {
        // Highlighting logic helper
        updateSizeHighlight();

        cardSize8.setOnClickListener(v -> {
            selectedSize = 8;
            basePrice = 8.00;
            updateSizeHighlight();
            calculateTotalPrice();
        });

        cardSize10.setOnClickListener(v -> {
            selectedSize = 10;
            basePrice = 10.00;
            updateSizeHighlight();
            calculateTotalPrice();
        });

        cardSize12.setOnClickListener(v -> {
            selectedSize = 12;
            basePrice = 12.00;
            updateSizeHighlight();
            calculateTotalPrice();
        });
    }

    private void updateSizeHighlight() {
        int primaryColor = getResources().getColor(R.color.primary);
        int surfaceColor = getResources().getColor(R.color.surface);

        cardSize8.setCardBackgroundColor(selectedSize == 8 ? primaryColor : surfaceColor);
        cardSize10.setCardBackgroundColor(selectedSize == 10 ? primaryColor : surfaceColor);
        cardSize12.setCardBackgroundColor(selectedSize == 12 ? primaryColor : surfaceColor);
    }

    private void fetchIngredients() {
        ApiClient.getApiService(this).getIngredients().enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ingredientList = response.body();
                    // Reset selected quantity just in case
                    for (Ingredient ing : ingredientList) {
                        ing.setSelectQuantity(0);
                    }
                    setupToppingsRecyclerView();
                } else {
                    Toast.makeText(CustomPizzaActivity.this, "Failed to load toppings", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Ingredient>> call, Throwable t) {
                Toast.makeText(CustomPizzaActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupToppingsRecyclerView() {
        adapter = new IngredientAdapter(ingredientList, (ingredient, newQty) -> {
            calculateTotalPrice();
        });
        rvToppings.setAdapter(adapter);
    }

    private void calculateTotalPrice() {
        double total = basePrice;
        for (Ingredient ing : ingredientList) {
            if (ing.getSelectQuantity() > 0) {
                total += ing.getSelectQuantity() * ing.getExtraPrice();
            }
        }
        currentTotalPrice = total;
        tvTotalPrice.setText(String.format("$%.2f", total));
    }

    private void submitCustomPizza() {
        // Collect selected ingredients
        List<CustomPizzaRequest.PizzaIngredient> requestIngredients = new ArrayList<>();
        for (Ingredient ing : ingredientList) {
            if (ing.getSelectQuantity() > 0) {
                requestIngredients.add(new CustomPizzaRequest.PizzaIngredient(ing.getId(), ing.getSelectQuantity()));
            }
        }

        CustomPizzaRequest pizzaRequest = new CustomPizzaRequest(selectedSize, requestIngredients);
        CartManager.getInstance().addCustomPizza(pizzaRequest, currentTotalPrice);

        Toast.makeText(this, "Custom Pizza added to cart!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
