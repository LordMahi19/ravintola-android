package com.example.ravintolaapp.models;

public class RegisterResponse {
    private String message;
    private User user;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
