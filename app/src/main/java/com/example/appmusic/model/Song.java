package com.example.appmusic.model;

public class Song {
    private int id;
    private String title;
    private int duration;
    private String filepath;
    private int genreId;
    private int artistId;
    private String imagePath;
    private String artistName;
    private String albumName;

    private boolean isFavorite;



    public Song(int id, String title, int duration, String filepath, String artistName, String imagePath) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.filepath = filepath;
        this.artistName = artistName;
        this.albumName = albumName;
        this.imagePath = imagePath;

    }

    public Song(int id, String title, int duration, String filepath, int genreId, int artistId, String imagePth) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.filepath = filepath;
        this.genreId = genreId;
        this.artistId = artistId;
        this.imagePath = imagePth;
    }


    public Song(String title, int duration, String filepath, int genreId, int artistId, int albumId) {
        this.title = title;
        this.duration = duration;
        this.filepath = filepath;
        this.genreId = genreId;
        this.artistId = artistId;}

    public Song(int id, String title, int duration, String filepath,
                String artistName, String albumName, String imagePath, int artistId) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.filepath = filepath;
        this.artistName = artistName;
        this.albumName = albumName;
        this.imagePath = imagePath;
        this.artistId = artistId;
    }

    public Song(int id, String title, String artistName, String imagePath, boolean isFavorite) {
        this.id = id;
        this.title = title;
        this.artistName = artistName;
        this.imagePath = imagePath;
        this.isFavorite = isFavorite;
    }




    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getFilepath() { return filepath; }
    public void setFilepath(String filepath) { this.filepath = filepath; }

    public int getGenreId() { return genreId; }
    public void setGenreId(int genreId) { this.genreId = genreId; }

    public int getArtistId() { return artistId; }
    public void setArtistId(int artistId) { this.artistId = artistId; }

    public String getArtistName() { return artistName; }
    public String getAlbumName() { return albumName; }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
