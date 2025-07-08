package com.example.appmusic.model;

public class History {
    private int id;
    private int songId;
    private String playedAt;

    public History(int id,  int songId, String playedAt) {
        this.id = id;

        this.songId = songId;
        this.playedAt = playedAt;
    }

    public History( int songId, String playedAt) {

        this.songId = songId;
        this.playedAt = playedAt;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSongId() { return songId; }
    public void setSongId(int songId) { this.songId = songId; }

    public String getPlayedAt() { return playedAt; }
    public void setPlayedAt(String playedAt) { this.playedAt = playedAt; }
}
