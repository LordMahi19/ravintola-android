package com.example.ravintolaapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravintolaapp.R;
import com.example.ravintolaapp.models.OrderItem;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    private final List<OrderItem> items;

    public OrderItemAdapter(List<OrderItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem item = items.get(position);

        holder.tvQty.setText(item.getQuantity() + "x");
        holder.tvPrice.setText(String.format("$%.2f", item.getLinePrice()));

        if (item.getMenuItemId() != null) {
            holder.tvTitle.setText(item.getMenuItemName());
            holder.tvCategory.setText(item.getMenuItemCategory());
        } else {
            // Custom pizza
            String sizeStr = item.getCustomPizzaSize() != null ? item.getCustomPizzaSize() + "\"" : "";
            holder.tvTitle.setText("Custom Pizza (" + sizeStr + ")");
            holder.tvCategory.setText("Custom pizza build");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQty;
        TextView tvTitle;
        TextView tvCategory;
        TextView tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQty = itemView.findViewById(R.id.tv_detail_item_qty);
            tvTitle = itemView.findViewById(R.id.tv_detail_item_title);
            tvCategory = itemView.findViewById(R.id.tv_detail_item_category);
            tvPrice = itemView.findViewById(R.id.tv_detail_item_price);
        }
    }
}
