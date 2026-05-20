package com.example.ravintolaapp.models;

public class DietLabel {
    private int id;
    private String label;

    public DietLabel(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}
