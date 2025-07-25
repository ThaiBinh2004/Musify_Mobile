package com.example.appmusic.model;


public class Artist {
    private int id;
    private String name;
    private String image;

    public Artist() {}

    public Artist(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

