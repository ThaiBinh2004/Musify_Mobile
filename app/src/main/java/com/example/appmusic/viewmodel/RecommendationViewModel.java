package com.example.appmusic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.appmusic.model.Song;
import com.example.appmusic.repository.SongRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecommendationViewModel extends AndroidViewModel {
    private final SongRepository songRepository;
    private final MutableLiveData<List<Song>> historySongs = new MutableLiveData<>();
    private final MediatorLiveData<List<Song>> recommendedSongs = new MediatorLiveData<>();

    public RecommendationViewModel(@NonNull Application application) {
        super(application);
        songRepository = new SongRepository(application);
        loadRecommendations(); // Tự động load khi ViewModel tạo
    }

    public LiveData<List<Song>> getRecommendedSongs() {
        return recommendedSongs;
    }

    private void loadRecommendations() {
        List<Song> allSongs = songRepository.getAllSongs();
        Collections.shuffle(allSongs); // trộn ngẫu nhiên

        List<Song> top5 = new ArrayList<>();
        for (int i = 0; i < Math.min(5, allSongs.size()); i++) {
            top5.add(allSongs.get(i));
        }

        recommendedSongs.setValue(top5);
    }

    private void updateRecommendedSongs(List<Song> history) {
        List<Song> allSongs = songRepository.getAllSongs();
        List<Song> recommended = new ArrayList<>();

        Collections.shuffle(allSongs);


        for (Song song : allSongs) {
            boolean isInHistory = history.stream().anyMatch(h -> h.getId() == song.getId());
            if (!isInHistory) {
                recommended.add(song);
            }

            if (recommended.size() >= 10) break; // chỉ lấy 10 bài
        }

        recommendedSongs.setValue(recommended);
    }


}
