package com.example.appmusic.model;

public class Genre {
    private int id;
    private String image;
    private String name;

    public Genre(int id, String image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }



    // Getter và Setter
    public int getId() { return id; }
    public String getImage() { return image; }
    public String getName() { return name; }

    public void setId(int id) { this.id = id; }
    public void setImage(String image) { this.image = image; }
    public void setName(String name) { this.name = name; }
}
