package com.example.ravintolaapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravintolaapp.R;
import com.example.ravintolaapp.models.MenuCategory;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(MenuCategory category, boolean isSelected);
    }

    private final List<MenuCategory> categories;
    private final OnCategoryClickListener listener;
    private int selectedPosition = -1; // -1 means none selected (All)

    public CategoryAdapter(List<MenuCategory> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuCategory category = categories.get(position);
        holder.tvChip.setText(category.getCategoryName());
        
        // Update selection visual state
        holder.tvChip.setSelected(selectedPosition == position);

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            if (selectedPosition == position) {
                // Deselect if clicked again
                selectedPosition = -1;
                notifyItemChanged(position);
                listener.onCategoryClick(null, false);
            } else {
                selectedPosition = position;
                notifyItemChanged(previousSelected);
                notifyItemChanged(selectedPosition);
                listener.onCategoryClick(category, true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void clearSelection() {
        int prev = selectedPosition;
        selectedPosition = -1;
        if (prev != -1) {
            notifyItemChanged(prev);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvChip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChip = itemView.findViewById(R.id.tv_chip_text);
        }
    }
}
