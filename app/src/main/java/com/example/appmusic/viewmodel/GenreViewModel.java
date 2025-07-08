package com.example.appmusic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.appmusic.model.Genre;
import com.example.appmusic.repository.GenreRepository;

import java.util.List;

public class GenreViewModel extends AndroidViewModel {
    private final GenreRepository repository;
    private final MutableLiveData<List<Genre>> genreList = new MutableLiveData<>();

    public GenreViewModel(@NonNull Application application) {
        super(application);
        repository = new GenreRepository(application);
        loadGenres();
    }

    public void loadGenres() {
        genreList.setValue(repository.getAllGenres());
    }

    public LiveData<List<Genre>> getGenreList() {
        return genreList;
    }

    public void addGenre(Genre genre) {
        repository.insertGenre(genre);
        loadGenres();
    }
}
