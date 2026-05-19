package com.example.ravintolaapp.models;

public class MenuItem {
    private int id;
    private String name;
    private String description;
    private double base_price;
    private int category_id;
    private Integer diet_type_id;
    private String image_base64;
    private boolean is_available;
    private String category_name;
    private String diet_label;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getBasePrice() { return base_price; }
    public void setBasePrice(double base_price) { this.base_price = base_price; }

    public int getCategoryId() { return category_id; }
    public void setCategoryId(int category_id) { this.category_id = category_id; }

    public Integer getDietTypeId() { return diet_type_id; }
    public void setDietTypeId(Integer diet_type_id) { this.diet_type_id = diet_type_id; }

    public String getImageBase64() { return image_base64; }
    public void setImageBase64(String image_base64) { this.image_base64 = image_base64; }

    public boolean isAvailable() { return is_available; }
    public void setAvailable(boolean available) { is_available = available; }

    public String getCategoryName() { return category_name; }
    public void setCategoryName(String category_name) { this.category_name = category_name; }

    public String getDietLabel() { return diet_label; }
    public void setDietLabel(String diet_label) { this.diet_label = diet_label; }
}
