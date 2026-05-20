package com.example.ravintolaapp.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ravintolaapp.R;
import com.example.ravintolaapp.models.MenuItem;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {

    public interface OnMenuItemClickListener {
        void onItemClick(MenuItem item);
        void onAddToCartClick(MenuItem item);
    }

    private final List<MenuItem> items;
    private final OnMenuItemClickListener listener;

    public MenuItemAdapter(List<MenuItem> items, OnMenuItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem item = items.get(position);

        holder.tvTitle.setText(item.getName());
        holder.tvDesc.setText(item.getCategoryName());
        holder.tvPrice.setText(String.format("$%.2f", item.getBasePrice()));

        // Display diet badge if available
        if (item.getDietLabel() != null && !item.getDietLabel().isEmpty()) {
            holder.tvDietBadge.setVisibility(View.VISIBLE);
            holder.tvDietBadge.setText(item.getDietLabel().toUpperCase());
        } else {
            holder.tvDietBadge.setVisibility(View.GONE);
        }

        // Determine local fallback placeholder based on category name
        int placeholderId = R.drawable.food_pizza;
        String catName = item.getCategoryName() != null ? item.getCategoryName().toLowerCase() : "";
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
        if (item.getImageBase64() != null && !item.getImageBase64().isEmpty()) {
            try {
                // If it starts with data URI prefix, strip it
                String base64Data = item.getImageBase64();
                if (base64Data.contains(",")) {
                    base64Data = base64Data.substring(base64Data.indexOf(",") + 1);
                }
                byte[] decodedString = Base64.decode(base64Data, Base64.DEFAULT);
                Glide.with(holder.itemView.getContext())
                        .load(decodedString)
                        .placeholder(placeholderId)
                        .error(placeholderId)
                        .into(holder.ivFoodPic);
            } catch (Exception e) {
                Glide.with(holder.itemView.getContext()).load(placeholderId).into(holder.ivFoodPic);
            }
        } else {
            Glide.with(holder.itemView.getContext()).load(placeholderId).into(holder.ivFoodPic);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.btnAddToCart.setOnClickListener(v -> listener.onAddToCartClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoodPic;
        TextView tvDietBadge;
        TextView tvTitle;
        TextView tvDesc;
        TextView tvPrice;
        ImageButton btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoodPic = itemView.findViewById(R.id.iv_food_pic);
            tvDietBadge = itemView.findViewById(R.id.tv_diet_badge);
            tvTitle = itemView.findViewById(R.id.tv_food_title);
            tvDesc = itemView.findViewById(R.id.tv_food_desc);
            tvPrice = itemView.findViewById(R.id.tv_food_price);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}
