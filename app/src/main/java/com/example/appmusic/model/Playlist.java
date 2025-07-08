package com.example.appmusic.model;

public class Playlist {
    private int id;
    private String name;
    private int userId;
    private String image;

    public Playlist(int id, String name, int userId, String image) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.image = image;
    }

    public Playlist(String name, int userId, String image) {
        this.name = name;
        this.userId = userId;
        this.image = image;
    }
    public Playlist(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getUserId() { return userId; }
    public String getImage() { return image; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setImage(String image) { this.image = image; }
}