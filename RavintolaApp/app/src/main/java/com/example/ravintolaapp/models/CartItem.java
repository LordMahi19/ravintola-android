package com.example.ravintolaapp.models;

public class CartItem {
    private MenuItem menuItem; // Not null for standard items
    private CustomPizzaRequest customPizza; // Not null for custom pizzas
    private int quantity;
    private double customPizzaPrice; // Local calculated price for custom pizza

    // Constructor for standard menu items
    public CartItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    // Constructor for custom pizzas
    public CartItem(CustomPizzaRequest customPizza, int quantity, double price) {
        this.customPizza = customPizza;
        this.quantity = quantity;
        this.customPizzaPrice = price;
    }

    public MenuItem getMenuItem() { return menuItem; }
    public void setMenuItem(MenuItem menuItem) { this.menuItem = menuItem; }

    public CustomPizzaRequest getCustomPizza() { return customPizza; }
    public void setCustomPizza(CustomPizzaRequest customPizza) { this.customPizza = customPizza; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getCustomPizzaPrice() { return customPizzaPrice; }
    public void setCustomPizzaPrice(double customPizzaPrice) { this.customPizzaPrice = customPizzaPrice; }

    public boolean isCustomPizza() {
        return customPizza != null;
    }

    public String getName() {
        if (isCustomPizza()) {
            return "Custom Pizza (" + customPizza.getSize() + "\")";
        }
        return menuItem.getName();
    }

    public double getUnitPrice() {
        if (isCustomPizza()) {
            return customPizzaPrice;
        }
        return menuItem.getBasePrice();
    }

    public double getTotalPrice() {
        return getUnitPrice() * quantity;
    }
}
