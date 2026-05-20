package com.example.ravintolaapp.utils;

import com.example.ravintolaapp.models.CartItem;
import com.example.ravintolaapp.models.CustomPizzaRequest;
import com.example.ravintolaapp.models.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addMenuItem(MenuItem item, int quantity) {
        // Check if item already in cart
        for (CartItem ci : cartItems) {
            if (!ci.isCustomPizza() && ci.getMenuItem().getId() == item.getId()) {
                ci.setQuantity(ci.getQuantity() + quantity);
                return;
            }
        }
        cartItems.add(new CartItem(item, quantity));
    }

    public void addCustomPizza(CustomPizzaRequest pizza, double calculatedPrice) {
        // Custom pizzas are usually unique, add directly
        cartItems.add(new CartItem(pizza, 1, calculatedPrice));
    }

    public void removeCartItem(CartItem item) {
        cartItems.remove(item);
    }

    public void updateQuantity(CartItem item, int quantity) {
        if (quantity <= 0) {
            removeCartItem(item);
        } else {
            item.setQuantity(quantity);
        }
    }

    public double getTotalPrice() {
        double total = 0.0;
        for (CartItem ci : cartItems) {
            total += ci.getTotalPrice();
        }
        return total;
    }

    public int getItemCount() {
        int count = 0;
        for (CartItem ci : cartItems) {
            count += ci.getQuantity();
        }
        return count;
    }

    public void clearCart() {
        cartItems.clear();
    }
}
