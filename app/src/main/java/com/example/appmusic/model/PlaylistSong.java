package com.example.appmusic.model;

public class PlaylistSong {
    private int id;
    private int playlistId;
    private int songId;

    public PlaylistSong(int id, int playlistId, int songId) {
        this.id = id;
        this.playlistId = playlistId;
        this.songId = songId;
    }

    public PlaylistSong(int playlistId, int songId) {
        this.playlistId = playlistId;
        this.songId = songId;
    }

    public int getId() { return id; }
    public int getPlaylistId() { return playlistId; }
    public int getSongId() { return songId; }

    public void setId(int id) { this.id = id; }
    public void setPlaylistId(int playlistId) { this.playlistId = playlistId; }
    public void setSongId(int songId) { this.songId = songId; }
}