package com.example.ravintolaapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravintolaapp.R;
import com.example.ravintolaapp.models.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    public interface OnIngredientQtyChangeListener {
        void onQtyChange(Ingredient ingredient, int newQty);
    }

    private final List<Ingredient> ingredients;
    private final OnIngredientQtyChangeListener listener;

    public IngredientAdapter(List<Ingredient> ingredients, OnIngredientQtyChangeListener listener) {
        this.ingredients = ingredients;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);

        holder.tvName.setText(ingredient.getName());
        holder.tvCategory.setText(ingredient.getCategoryName());
        
        if (ingredient.getExtraPrice() == 0) {
            holder.tvPrice.setText("Free");
            holder.tvPrice.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.status_completed));
        } else {
            holder.tvPrice.setText(String.format("+$%.2f", ingredient.getExtraPrice()));
            holder.tvPrice.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.accent));
        }

        holder.tvQty.setText(String.valueOf(ingredient.getSelectQuantity()));

        holder.btnPlus.setOnClickListener(v -> {
            int newQty = ingredient.getSelectQuantity() + 1;
            ingredient.setSelectQuantity(newQty);
            holder.tvQty.setText(String.valueOf(newQty));
            listener.onQtyChange(ingredient, newQty);
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (ingredient.getSelectQuantity() > 0) {
                int newQty = ingredient.getSelectQuantity() - 1;
                ingredient.setSelectQuantity(newQty);
                holder.tvQty.setText(String.valueOf(newQty));
                listener.onQtyChange(ingredient, newQty);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvCategory;
        TextView tvPrice;
        TextView tvQty;
        ImageButton btnMinus;
        ImageButton btnPlus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_ingredient_name);
            tvCategory = itemView.findViewById(R.id.tv_ingredient_category);
            tvPrice = itemView.findViewById(R.id.tv_ingredient_price);
            tvQty = itemView.findViewById(R.id.tv_topping_qty);
            btnMinus = itemView.findViewById(R.id.btn_topping_minus);
            btnPlus = itemView.findViewById(R.id.btn_topping_plus);
        }
    }
}
