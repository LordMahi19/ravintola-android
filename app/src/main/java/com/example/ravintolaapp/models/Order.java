package com.example.ravintolaapp.models;

import java.util.List;

public class Order {
    private int id;
    private Integer user_id; // Can be null for guests
    private double total_price;
    private String status;
    private String created_at;
    private String username; // Present when fetched for admin/staff, otherwise optional
    private List<OrderItem> items;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getUserId() { return user_id; }
    public void setUserId(Integer user_id) { this.user_id = user_id; }

    public double getTotalPrice() { return total_price; }
    public void setTotalPrice(double total_price) { this.total_price = total_price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return created_at; }
    public void setCreatedAt(String created_at) { this.created_at = created_at; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
