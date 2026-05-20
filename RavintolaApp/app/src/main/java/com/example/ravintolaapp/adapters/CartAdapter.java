package com.example.ravintolaapp.adapters;

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
import com.example.ravintolaapp.models.CartItem;
import com.example.ravintolaapp.models.MenuItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    public interface OnCartItemChangedListener {
        void onItemDeleted(CartItem item);
        void onItemQtyChanged(CartItem item, int newQty);
    }

    private final List<CartItem> cartItems;
    private final OnCartItemChangedListener listener;

    public CartAdapter(List<CartItem> cartItems, OnCartItemChangedListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.tvTitle.setText(item.getName());
        holder.tvSubtitle.setText(String.format("%d x $%.2f", item.getQuantity(), item.getUnitPrice()));
        holder.tvQty.setText(String.valueOf(item.getQuantity()));
        holder.tvSubtotal.setText(String.format("$%.2f", item.getTotalPrice()));

        // Display images appropriately
        if (item.isCustomPizza()) {
            // Load custom pizza local banner/image
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.food_custom_pizza)
                    .into(holder.ivFoodPic);
        } else {
            // Load standard menu item base64 or placeholder
            MenuItem menuItem = item.getMenuItem();
            int placeholderId = R.drawable.food_pizza;
            String catName = menuItem.getCategoryName() != null ? menuItem.getCategoryName().toLowerCase() : "";
            if (catName.contains("kebab")) {
                placeholderId = R.drawable.food_kebab;
            } else if (catName.contains("salad")) {
                placeholderId = R.drawable.food_salad;
            } else if (catName.contains("drink") || catName.contains("beverage")) {
                placeholderId = R.drawable.food_drink;
            } else if (catName.contains("dessert") || catName.contains("cake")) {
                placeholderId = R.drawable.food_dessert;
            }

            if (menuItem.getImageBase64() != null && !menuItem.getImageBase64().isEmpty()) {
                try {
                    String base64Data = menuItem.getImageBase64();
                    if (base64Data.contains(",")) {
                        base64Data = base64Data.substring(base64Data.indexOf(",") + 1);
                    }
                    byte[] decodedString = android.util.Base64.decode(base64Data, android.util.Base64.DEFAULT);
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
        }

        holder.btnPlus.setOnClickListener(v -> {
            int newQty = item.getQuantity() + 1;
            item.setQuantity(newQty);
            notifyItemChanged(position);
            listener.onItemQtyChanged(item, newQty);
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                int newQty = item.getQuantity() - 1;
                item.setQuantity(newQty);
                notifyItemChanged(position);
                listener.onItemQtyChanged(item, newQty);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            listener.onItemDeleted(item);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoodPic;
        TextView tvTitle;
        TextView tvSubtitle;
        TextView tvQty;
        TextView tvSubtotal;
        ImageButton btnMinus;
        ImageButton btnPlus;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoodPic = itemView.findViewById(R.id.iv_cart_food_pic);
            tvTitle = itemView.findViewById(R.id.tv_cart_item_title);
            tvSubtitle = itemView.findViewById(R.id.tv_cart_item_subtitle);
            tvQty = itemView.findViewById(R.id.tv_cart_qty);
            tvSubtotal = itemView.findViewById(R.id.tv_cart_item_subtotal);
            btnMinus = itemView.findViewById(R.id.btn_cart_minus);
            btnPlus = itemView.findViewById(R.id.btn_cart_plus);
            btnDelete = itemView.findViewById(R.id.btn_cart_delete);
        }
    }
}
