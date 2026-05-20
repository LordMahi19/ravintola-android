package com.example.ravintolaapp.models;

public class Ingredient {
    private int id;
    private String name;
    private int category_id;
    private double extra_price;
    private boolean is_available;
    private String category_name;

    // Field to track user selection quantity locally in custom pizza builder
    private int selectQuantity = 0;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCategoryId() { return category_id; }
    public void setCategoryId(int category_id) { this.category_id = category_id; }

    public double getExtraPrice() { return extra_price; }
    public void setExtraPrice(double extra_price) { this.extra_price = extra_price; }

    public boolean isAvailable() { return is_available; }
    public void setAvailable(boolean available) { is_available = available; }

    public String getCategoryName() { return category_name; }
    public void setCategoryName(String category_name) { this.category_name = category_name; }

    // local quantity tracking
    public int getSelectQuantity() { return selectQuantity; }
    public void setSelectQuantity(int selectQuantity) { this.selectQuantity = selectQuantity; }
}
