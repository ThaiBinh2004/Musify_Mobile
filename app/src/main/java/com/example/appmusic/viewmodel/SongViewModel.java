package com.example.appmusic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.appmusic.model.Song;
import com.example.appmusic.repository.SongRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class SongViewModel extends AndroidViewModel {
    private final SongRepository songRepository;
    private final MutableLiveData<List<Song>> songs = new MutableLiveData<>();

    public SongViewModel(@NonNull Application application) {
        super(application);
        songRepository = new SongRepository(application);
        loadSongs();
    }

    public void loadSongs() {
        songs.setValue(songRepository.getAllSongs());
    }

    public LiveData<List<Song>> getSongs() {
        return songs;
    }

    public void addSong(Song song) {
        songRepository.insertSong(song);
        loadSongs();
    }

    public void updateSong(Song song) {
        songRepository.updateSong(song);
        loadSongs();
    }

    public void deleteSong(int songId) {
        songRepository.deleteSong(songId);
        loadSongs();
    }

    public LiveData<List<Song>> getSongsByArtistId(int artistId) {
        MutableLiveData<List<Song>> data = new MutableLiveData<>();
        data.setValue(songRepository.getSongsByArtistId(artistId));
        return data;
    }

    public LiveData<List<Song>> getSongsByGenreId(int genreId) {
        MutableLiveData<List<Song>> data = new MutableLiveData<>();
        data.setValue(songRepository.getSongsByGenreId(genreId));
        return data;
    }

    public LiveData<List<Song>> getFavoriteSongs(int userId) {
        MutableLiveData<List<Song>> data = new MutableLiveData<>();
        data.setValue(songRepository.getFavoriteSongs(userId));
        return data;
    }

    public LiveData<List<Song>> searchSongsByTitle(String keyword) {
        MutableLiveData<List<Song>> result = new MutableLiveData<>();
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Song> data = songRepository.searchSongsByTitle(keyword);
            result.postValue(data);
        });
        return result;
    }







}
