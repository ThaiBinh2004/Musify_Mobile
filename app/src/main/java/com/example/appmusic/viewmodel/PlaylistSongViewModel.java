package com.example.appmusic.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.appmusic.model.PlaylistSong;
import com.example.appmusic.repository.PlaylistSongRepository;

import java.util.List;

public class PlaylistSongViewModel extends AndroidViewModel {
    private final PlaylistSongRepository repository;
    private final MutableLiveData<List<PlaylistSong>> playlistSongs = new MutableLiveData<>();

    public PlaylistSongViewModel(@NonNull Application application) {
        super(application);
        repository = new PlaylistSongRepository(application);
    }

    public LiveData<List<PlaylistSong>> getPlaylistSongs() {
        return playlistSongs;
    }

    public void loadSongsByPlaylist(int playlistId) {
        playlistSongs.setValue(repository.getSongsByPlaylist(playlistId));
    }

    public void addPlaylistSong(PlaylistSong ps) {
        repository.insertPlaylistSong(ps);
        loadSongsByPlaylist(ps.getPlaylistId());
    }

    public void removePlaylistSong(int id, int playlistId) {
        repository.deletePlaylistSong(id);
        loadSongsByPlaylist(playlistId);
    }
}
