package com.example.appmusic.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.appmusic.model.Favorite;
import com.example.appmusic.repository.FavoriteRepository;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {

    private final FavoriteRepository repository;
    private final MutableLiveData<List<Favorite>> favoriteList = new MutableLiveData<>();
    private int currentUserId = 1; // Bạn có thể lấy user ID từ SharedPreferences nếu cần

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        repository = new FavoriteRepository(application);
        loadFavorites();
    }

    private void loadFavorites() {
        List<Favorite> list = repository.getFavoritesByUser(currentUserId);
        favoriteList.setValue(list);
    }

    public LiveData<List<Favorite>> getFavoriteList() {
        return favoriteList;
    }

    public boolean isFavorite(int songId) {
        List<Favorite> list = favoriteList.getValue();
        if (list != null) {
            for (Favorite fav : list) {
                if (fav.getSongId() == songId) return true;
            }
        }
        return false;
    }

    public void toggleFavorite(int songId) {
        List<Favorite> list = favoriteList.getValue();

        if (list != null) {
            // Tìm nếu đã tồn tại
            for (Favorite fav : list) {
                if (fav.getSongId() == songId) {
                    repository.deleteFavorite(fav.getId());
                    loadFavorites(); // cập nhật lại list
                    return;
                }
            }
        }

        // Nếu chưa tồn tại thì thêm mới
        Favorite newFav = new Favorite(currentUserId, songId);
        repository.insertFavorite(newFav);
        loadFavorites();
    }

    public void setUserId(int userId) {
        this.currentUserId = userId;
        loadFavorites();
    }
}

