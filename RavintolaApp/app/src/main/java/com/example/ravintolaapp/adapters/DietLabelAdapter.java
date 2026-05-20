package com.example.ravintolaapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravintolaapp.R;
import com.example.ravintolaapp.models.DietLabel;

import java.util.List;

public class DietLabelAdapter extends RecyclerView.Adapter<DietLabelAdapter.ViewHolder> {

    public interface OnDietClickListener {
        void onDietClick(DietLabel diet, boolean isSelected);
    }

    private final List<DietLabel> diets;
    private final OnDietClickListener listener;
    private int selectedPosition = -1;

    public DietLabelAdapter(List<DietLabel> diets, OnDietClickListener listener) {
        this.diets = diets;
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
        DietLabel diet = diets.get(position);
        holder.tvChip.setText(diet.getLabel());
        holder.tvChip.setSelected(selectedPosition == position);

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            if (selectedPosition == position) {
                selectedPosition = -1;
                notifyItemChanged(position);
                listener.onDietClick(null, false);
            } else {
                selectedPosition = position;
                notifyItemChanged(previousSelected);
                notifyItemChanged(selectedPosition);
                listener.onDietClick(diet, true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return diets.size();
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
