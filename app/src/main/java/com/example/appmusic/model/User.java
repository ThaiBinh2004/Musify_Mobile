package com.example.appmusic.model;

public class User {
    private int id;
    private String email;
    private String password;
    private String username;
    private String createdAt;

    public User() {}

    public User(String username, String email, String password, String createdAt) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    public User(int id, String username, String email, String password, String createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
