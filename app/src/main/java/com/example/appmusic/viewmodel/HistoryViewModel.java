package com.example.appmusic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.appmusic.model.Song;
import com.example.appmusic.repository.HistoryRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryViewModel extends AndroidViewModel {

    private final HistoryRepository historyRepository;
    private final MutableLiveData<List<Song>> historySongs = new MutableLiveData<>();

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public HistoryViewModel(@NonNull Application application) {
        super(application);
        historyRepository = new HistoryRepository(application);
        loadHistory(); // Load dữ liệu ngay khi khởi tạo
    }

    // Lấy danh sách lịch sử phát
    public LiveData<List<Song>> getHistorySongs() {
        return historySongs;
    }

    // Tải lại dữ liệu lịch sử
    public void loadHistory() {
        List<Song> songs = historyRepository.getAllHistorySongs();

        // An toàn hơn: chỉ lấy tối đa 5 bài nếu đủ
        if (songs.size() > 5) {
            historySongs.setValue(songs.subList(0, 5));
        } else {
            historySongs.setValue(songs);
        }
    }



    // Thêm 1 bài vào lịch sử
    public void addToHistory(int songId) {
        String playedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        historyRepository.insertHistory(songId, playedAt);
        loadHistory(); // cập nhật lại sau khi thêm
    }

    // Lấy bài mới nhất
    public Song getLastPlayedSong() {
        return historyRepository.getLastPlayedSong();
    }
}
