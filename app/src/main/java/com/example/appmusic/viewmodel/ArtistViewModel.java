package com.example.appmusic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.appmusic.model.Artist;
import com.example.appmusic.repository.ArtistRepository;

import java.util.List;

public class ArtistViewModel extends AndroidViewModel {
    private final ArtistRepository artistRepository;
    private final MutableLiveData<List<Artist>> artistList = new MutableLiveData<>();



    public ArtistViewModel(@NonNull Application application) {
        super(application);
        artistRepository = new ArtistRepository(application);
        loadArtists(); // Load mặc định
    }

    public void loadArtists() {
        List<Artist> list = artistRepository.getAllArtists();
        artistList.setValue(list);
    }

    public LiveData<List<Artist>> getArtistList() {
        return artistList;
    }

    public void addArtist(Artist artist) {
        artistRepository.insertArtist(artist);
        loadArtists(); // Refresh list
    }

    public void updateArtist(Artist artist) {
        artistRepository.updateArtist(artist);
        loadArtists(); // Refresh
    }

    public void deleteArtist(int artistId) {
        artistRepository.deleteArtist(artistId);
        loadArtists(); // Refresh
    }
}

