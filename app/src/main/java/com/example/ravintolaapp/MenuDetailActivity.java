package com.example.ravintolaapp;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ravintolaapp.models.MenuItem;
import com.example.ravintolaapp.utils.CartManager;

public class MenuDetailActivity extends AppCompatActivity {

    private ImageView ivHeroPic;
    private ImageButton btnBack, btnMinus, btnPlus;
    private TextView tvCategory, tvDiet, tvTitle, tvPrice, tvQty, tvDesc;
    private Button btnAddCart;

    private int itemId;
    private String itemName;
    private String itemCategory;
    private String itemDiet;
    private double itemPrice;
    private String itemDesc;
    private String itemImage;

    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        ivHeroPic = findViewById(R.id.iv_hero_food_pic);
        btnBack = findViewById(R.id.btn_back);
        btnMinus = findViewById(R.id.btn_qty_minus);
        btnPlus = findViewById(R.id.btn_qty_plus);
        tvCategory = findViewById(R.id.tv_detail_category);
        tvDiet = findViewById(R.id.tv_detail_diet);
        tvTitle = findViewById(R.id.tv_detail_title);
        tvPrice = findViewById(R.id.tv_detail_price);
        tvQty = findViewById(R.id.tv_qty_val);
        tvDesc = findViewById(R.id.tv_detail_desc);
        btnAddCart = findViewById(R.id.btn_add_cart_submit);

        // Retrieve passed Extras
        itemId = getIntent().getIntExtra("menu_item_id", -1);
        itemName = getIntent().getStringExtra("menu_item_name");
        itemCategory = getIntent().getStringExtra("menu_item_category");
        itemDiet = getIntent().getStringExtra("menu_item_diet");
        itemPrice = getIntent().getDoubleExtra("menu_item_price", 0.0);
        itemDesc = getIntent().getStringExtra("menu_item_desc");
        itemImage = getIntent().getStringExtra("menu_item_image");

        setupUI();
    }

    private void setupUI() {
        tvTitle.setText(itemName);
        tvCategory.setText(itemCategory != null ? itemCategory.toUpperCase() : "FOOD");
        tvDesc.setText(itemDesc != null ? itemDesc : "Gourmet freshly prepared restaurant dish.");
        tvPrice.setText(String.format("$%.2f", itemPrice));

        if (itemDiet != null && !itemDiet.isEmpty()) {
            tvDiet.setVisibility(View.VISIBLE);
            tvDiet.setText(itemDiet.toUpperCase());
            findViewById(R.id.divider_dot).setVisibility(View.VISIBLE);
        } else {
            tvDiet.setVisibility(View.GONE);
            findViewById(R.id.divider_dot).setVisibility(View.GONE);
        }

        // Determine fallback category image
        int placeholderId = R.drawable.food_pizza;
        String catName = itemCategory != null ? itemCategory.toLowerCase() : "";
        if (catName.contains("kebab")) {
            placeholderId = R.drawable.food_kebab;
        } else if (catName.contains("salad")) {
            placeholderId = R.drawable.food_salad;
        } else if (catName.contains("drink") || catName.contains("beverage")) {
            placeholderId = R.drawable.food_drink;
        } else if (catName.contains("dessert") || catName.contains("cake")) {
            placeholderId = R.drawable.food_dessert;
        }

        // Load image (Support base64 or fallback to drawable resource)
        if (itemImage != null && !itemImage.isEmpty()) {
            try {
                String base64Data = itemImage;
                if (base64Data.contains(",")) {
                    base64Data = base64Data.substring(base64Data.indexOf(",") + 1);
                }
                byte[] decodedString = Base64.decode(base64Data, Base64.DEFAULT);
                Glide.with(this)
                        .load(decodedString)
                        .placeholder(placeholderId)
                        .error(placeholderId)
                        .into(ivHeroPic);
            } catch (Exception e) {
                Glide.with(this).load(placeholderId).into(ivHeroPic);
            }
        } else {
            Glide.with(this).load(placeholderId).into(ivHeroPic);
        }

        // Navigation
        btnBack.setOnClickListener(v -> finish());

        // Quantity Adjuster
        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQty.setText(String.valueOf(quantity));
            updatePrice();
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQty.setText(String.valueOf(quantity));
                updatePrice();
            }
        });

        // Add to Cart
        btnAddCart.setOnClickListener(v -> {
            MenuItem item = new MenuItem();
            item.setId(itemId);
            item.setName(itemName);
            item.setBasePrice(itemPrice);
            item.setCategoryName(itemCategory);
            item.setDietLabel(itemDiet);
            item.setImageBase64(itemImage);

            CartManager.getInstance().addMenuItem(item, quantity);
            Toast.makeText(this, quantity + "x " + itemName + " added to cart!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void updatePrice() {
        tvPrice.setText(String.format("$%.2f", itemPrice * quantity));
    }
}
