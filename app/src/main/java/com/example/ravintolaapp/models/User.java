package com.example.ravintolaapp.models;

public class User {
    private int id;
    private String username;
    private String role;
    private String created_at;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getCreatedAt() { return created_at; }
    public void setCreatedAt(String created_at) { this.created_at = created_at; }
}
