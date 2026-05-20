package com.example.ravintolaapp.models;

public class OrderItem {
    private int id;
    private int order_id;
    private Integer menu_item_id;
    private Integer custom_pizza_id;
    private int quantity;
    private double line_price;
    
    // Joined columns
    private String menu_item_name;
    private String menu_item_category;
    private String custom_pizza_size;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return order_id; }
    public void setOrderId(int order_id) { this.order_id = order_id; }

    public Integer getMenuItemId() { return menu_item_id; }
    public void setMenuItemId(Integer menu_item_id) { this.menu_item_id = menu_item_id; }

    public Integer getCustomPizzaId() { return custom_pizza_id; }
    public void setCustomPizzaId(Integer custom_pizza_id) { this.custom_pizza_id = custom_pizza_id; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getLinePrice() { return line_price; }
    public void setLinePrice(double line_price) { this.line_price = line_price; }

    public String getMenuItemName() { return menu_item_name; }
    public void setMenuItemName(String menu_item_name) { this.menu_item_name = menu_item_name; }

    public String getMenuItemCategory() { return menu_item_category; }
    public void setMenuItemCategory(String menu_item_category) { this.menu_item_category = menu_item_category; }

    public String getCustomPizzaSize() { return custom_pizza_size; }
    public void setCustomPizzaSize(String custom_pizza_size) { this.custom_pizza_size = custom_pizza_size; }
}
