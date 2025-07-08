package com.example.appmusic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.appmusic.model.Playlist;
import com.example.appmusic.repository.PlaylistRepository;

import java.util.List;

public class PlaylistViewModel extends AndroidViewModel {
    private final PlaylistRepository repository;
    private final MutableLiveData<List<Playlist>> playlists = new MutableLiveData<>();

    public PlaylistViewModel(@NonNull Application application) {
        super(application);
        repository = new PlaylistRepository(application);
    }

    public LiveData<List<Playlist>> getPlaylists() {
        return playlists;
    }

    public void loadPlaylistsByUser(int userId) {
        playlists.setValue(repository.getPlaylistsByUser(userId));
    }

    public void addPlaylist(Playlist playlist) {
        repository.insertPlaylist(playlist);
        loadPlaylistsByUser(playlist.getUserId());
    }

    public void updatePlaylist(Playlist playlist) {
        repository.updatePlaylist(playlist);
        loadPlaylistsByUser(playlist.getUserId());
    }

    public void deletePlaylist(int id, int userId) {
        repository.deletePlaylist(id);
        loadPlaylistsByUser(userId);
    }
}
