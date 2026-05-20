package com.example.ravintolaapp.models;

import java.util.List;

public class OrderRequest {
    private List<OrderItemRequest> items;

    public OrderRequest(List<OrderItemRequest> items) {
        this.items = items;
    }

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }

    public static class OrderItemRequest {
        private Integer menu_item_id;
        private int quantity;
        private CustomPizzaRequest custom_pizza;

        // Constructor for standard menu items
        public OrderItemRequest(int menu_item_id, int quantity) {
            this.menu_item_id = menu_item_id;
            this.quantity = quantity;
        }

        // Constructor for custom pizzas
        public OrderItemRequest(CustomPizzaRequest custom_pizza, int quantity) {
            this.custom_pizza = custom_pizza;
            this.quantity = quantity;
        }

        public Integer getMenuItemId() { return menu_item_id; }
        public void setMenuItemId(Integer menu_item_id) { this.menu_item_id = menu_item_id; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public CustomPizzaRequest getCustomPizza() { return custom_pizza; }
        public void setCustomPizza(CustomPizzaRequest custom_pizza) { this.custom_pizza = custom_pizza; }
    }
}
