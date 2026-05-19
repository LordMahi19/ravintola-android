package com.example.ravintolaapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravintolaapp.R;
import com.example.ravintolaapp.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    private final List<Order> orders;
    private final OnOrderClickListener listener;

    public OrderAdapter(List<Order> orders, OnOrderClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.tvOrderId.setText("Order #" + order.getId());
        holder.tvOrderPrice.setText(String.format("$%.2f", order.getTotalPrice()));
        
        // Format date string nicely if present, otherwise fallback
        String dateStr = order.getCreatedAt();
        if (dateStr != null && dateStr.length() > 10) {
            dateStr = dateStr.replace("T", " ").substring(0, 16);
        }
        holder.tvOrderDate.setText(dateStr != null ? dateStr : "Just now");

        // Map status to colored card backgrounds and black/white text
        String status = order.getStatus() != null ? order.getStatus().toLowerCase() : "pending";
        holder.tvOrderStatus.setText(status);

        int badgeColor;
        switch (status) {
            case "processing":
                badgeColor = holder.itemView.getContext().getResources().getColor(R.color.status_processing);
                break;
            case "completed":
                badgeColor = holder.itemView.getContext().getResources().getColor(R.color.status_completed);
                break;
            case "pending":
            default:
                badgeColor = holder.itemView.getContext().getResources().getColor(R.color.status_pending);
                break;
        }
        holder.cardStatusBadge.setCardBackgroundColor(badgeColor);

        // Summarize items count
        int itemsCount = 0;
        if (order.getItems() != null) {
            itemsCount = order.getItems().size();
        }
        holder.tvOrderSummary.setText(itemsCount == 1 ? "1 item • View details" : itemsCount + " items • View details");

        holder.itemView.setOnClickListener(v -> listener.onOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId;
        TextView tvOrderDate;
        TextView tvOrderSummary;
        TextView tvOrderPrice;
        TextView tvOrderStatus;
        CardView cardStatusBadge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id_title);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderSummary = itemView.findViewById(R.id.tv_order_summary_items);
            tvOrderPrice = itemView.findViewById(R.id.tv_order_total_price);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status_text);
            cardStatusBadge = itemView.findViewById(R.id.card_order_status_badge);
        }
    }
}
