package com.example.ravintolaapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravintolaapp.CustomPizzaActivity;
import com.example.ravintolaapp.MenuDetailActivity;
import com.example.ravintolaapp.R;
import com.example.ravintolaapp.adapters.CategoryAdapter;
import com.example.ravintolaapp.adapters.DietLabelAdapter;
import com.example.ravintolaapp.adapters.MenuItemAdapter;
import com.example.ravintolaapp.models.DietLabel;
import com.example.ravintolaapp.models.MenuCategory;
import com.example.ravintolaapp.models.MenuItem;
import com.example.ravintolaapp.network.ApiClient;
import com.example.ravintolaapp.utils.CartManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuFragment extends Fragment {

    private RecyclerView rvCategories, rvDietLabels, rvMenuItems;
    private CardView cardCustomPizzaBanner;
    private Button btnBannerBuild;

    private List<MenuCategory> categoryList = new ArrayList<>();
    private List<DietLabel> dietList = new ArrayList<>();
    private List<MenuItem> menuList = new ArrayList<>();

    private CategoryAdapter categoryAdapter;
    private DietLabelAdapter dietAdapter;
    private MenuItemAdapter menuAdapter;

    private String selectedCategory = null;
    private String selectedDiet = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        rvCategories = view.findViewById(R.id.rv_categories);
        rvDietLabels = view.findViewById(R.id.rv_diet_labels);
        rvMenuItems = view.findViewById(R.id.rv_menu_items);
        cardCustomPizzaBanner = view.findViewById(R.id.card_custom_pizza_banner);
        btnBannerBuild = view.findViewById(R.id.btn_banner_build);

        setupClickListeners();
        fetchFilters();
        fetchMenuItems();

        return view;
    }

    private void setupClickListeners() {
        View.OnClickListener launchCustomPizza = v -> {
            Intent intent = new Intent(getContext(), CustomPizzaActivity.class);
            startActivity(intent);
        };
        cardCustomPizzaBanner.setOnClickListener(launchCustomPizza);
        btnBannerBuild.setOnClickListener(launchCustomPizza);
    }

    private void fetchFilters() {
        // Fetch categories
        ApiClient.getApiService(getContext()).getMenuCategories().enqueue(new Callback<List<MenuCategory>>() {
            @Override
            public void onResponse(Call<List<MenuCategory>> call, Response<List<MenuCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList = response.body();
                    setupCategoryRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<List<MenuCategory>> call, Throwable t) {
                // Silently fallback or toast
            }
        });

        // Fetch diet labels
        ApiClient.getApiService(getContext()).getDietLabels().enqueue(new Callback<List<DietLabel>>() {
            @Override
            public void onResponse(Call<List<DietLabel>> call, Response<List<DietLabel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dietList = response.body();
                    setupDietRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<List<DietLabel>> call, Throwable t) {
                // Silently fallback or toast
            }
        });
    }

    private void fetchMenuItems() {
        ApiClient.getApiService(getContext()).getMenuItems(selectedCategory, selectedDiet).enqueue(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    menuList = response.body();
                    setupMenuRecyclerView();
                } else {
                    Toast.makeText(getContext(), "Failed to load menu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCategoryRecyclerView() {
        categoryAdapter = new CategoryAdapter(categoryList, (category, isSelected) -> {
            selectedCategory = isSelected ? category.getCategoryName() : null;
            fetchMenuItems();
        });
        rvCategories.setAdapter(categoryAdapter);
    }

    private void setupDietRecyclerView() {
        dietAdapter = new DietLabelAdapter(dietList, (diet, isSelected) -> {
            selectedDiet = isSelected ? diet.getLabel() : null;
            fetchMenuItems();
        });
        rvDietLabels.setAdapter(dietAdapter);
    }

    private void setupMenuRecyclerView() {
        menuAdapter = new MenuItemAdapter(menuList, new MenuItemAdapter.OnMenuItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                Intent intent = new Intent(getContext(), MenuDetailActivity.class);
                intent.putExtra("menu_item_id", item.getId());
                intent.putExtra("menu_item_name", item.getName());
                intent.putExtra("menu_item_category", item.getCategoryName());
                intent.putExtra("menu_item_diet", item.getDietLabel());
                intent.putExtra("menu_item_price", item.getBasePrice());
                intent.putExtra("menu_item_desc", item.getDescription());
                intent.putExtra("menu_item_image", item.getImageBase64());
                startActivity(intent);
            }

            @Override
            public void onAddToCartClick(MenuItem item) {
                CartManager.getInstance().addMenuItem(item, 1);
                Toast.makeText(getContext(), item.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
            }
        });
        rvMenuItems.setAdapter(menuAdapter);
    }
}
