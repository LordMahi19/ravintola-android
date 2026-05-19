package com.example.ravintolaapp.models;

import java.util.List;

public class CustomPizzaRequest {
    private int size; // 8, 10, or 12
    private List<PizzaIngredient> ingredients;

    public CustomPizzaRequest(int size, List<PizzaIngredient> ingredients) {
        this.size = size;
        this.ingredients = ingredients;
    }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public List<PizzaIngredient> getIngredients() { return ingredients; }
    public void setIngredients(List<PizzaIngredient> ingredients) { this.ingredients = ingredients; }

    public static class PizzaIngredient {
        private int ingredient_id;
        private int quantity;

        public PizzaIngredient(int ingredient_id, int quantity) {
            this.ingredient_id = ingredient_id;
            this.quantity = quantity;
        }

        public int getIngredientId() { return ingredient_id; }
        public void setIngredientId(int ingredient_id) { this.ingredient_id = ingredient_id; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}
