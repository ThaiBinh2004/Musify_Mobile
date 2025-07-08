package com.example.appmusic.viewmodel;

import android.app.Application;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.appmusic.model.Song;
import com.example.appmusic.repository.SongRepository;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class NowPlayingViewModel extends AndroidViewModel {
    private final SongRepository songRepository;
    private final MutableLiveData<List<Song>> songList = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentIndex = new MutableLiveData<>(0);
    private final MutableLiveData<Song> currentSong = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isPrepared = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isPlaying = new MutableLiveData<>(false);

    public LiveData<Boolean> getIsPlaying() {
        return isPlaying;
    }


    private MediaPlayer mediaPlayer;
    private AssetManager assetManager;
    private int lastPlayedId = -1;

    private boolean isRepeat = false;
    private boolean isShuffle = false;

    public NowPlayingViewModel(@NonNull Application application) {
        super(application);
        this.assetManager = application.getAssets();
        songRepository = new SongRepository(application);
        songList.setValue(songRepository.getAllSongs());
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public LiveData<List<Song>> getSongList() {
        return songList;
    }

    public LiveData<Song> getCurrentSong() {
        return currentSong;
    }

    public LiveData<Integer> getCurrentIndex() {
        return currentIndex;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public LiveData<Boolean> isPrepared() {
        return isPrepared;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void toggleRepeat() {
        isRepeat = !isRepeat;
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public void toggleShuffle() {
        isShuffle = !isShuffle;
    }

    public void playSongById(int songId) {
        List<Song> songs = songList.getValue();
        if (songs == null || songs.isEmpty()) return;

        for (int i = 0; i < songs.size(); i++) {
            Song song = songs.get(i);
            if (song.getId() == songId) {
                if (mediaPlayer != null && lastPlayedId == songId && mediaPlayer.isPlaying()) {
                    return;
                }

                releasePlayer();

                currentIndex.setValue(i);
                currentSong.setValue(song);
                lastPlayedId = songId;

                try {
                    AssetFileDescriptor afd = assetManager.openFd(song.getFilepath());
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

                    mediaPlayer.setOnPreparedListener(mp -> {
                        mp.start();
                        isPrepared.setValue(true);
                        isPlaying.setValue(true);
                    });

                    mediaPlayer.setOnCompletionListener(mp -> handleSongCompletion());
                    mediaPlayer.prepareAsync();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            }
        }
    }

    private void handleSongCompletion() {
        releasePlayer(); // Đảm bảo trạng thái sạch trước khi phát mới

        List<Song> songs = songList.getValue();
        if (songs == null || songs.isEmpty()) return;

        int index = currentIndex.getValue() != null ? currentIndex.getValue() : 0;
        int nextIndex;

        if (isRepeat) {
            nextIndex = index;
        } else if (isShuffle) {
            Random random = new Random();
            do {
                nextIndex = random.nextInt(songs.size());
            } while (songs.size() > 1 && nextIndex == index);
        } else {
            nextIndex = (index + 1) % songs.size();
        }

        playSongById(songs.get(nextIndex).getId());
    }

    public void playPreviousSong() {
        List<Song> songs = songList.getValue();
        if (songs == null || songs.isEmpty()) return;

        int index = currentIndex.getValue() != null ? currentIndex.getValue() : 0;
        int prevIndex = (index - 1 + songs.size()) % songs.size();
        playSongById(songs.get(prevIndex).getId());
    }

    public void togglePlayPause() {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    isPlaying.setValue(false);
                } else {
                    mediaPlayer.start();
                    isPlaying.setValue(true);
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace(); // Tránh crash nếu gọi sai thời điểm
        }
    }

    public boolean isPlaying() {
        try {
            return mediaPlayer != null && mediaPlayer.isPlaying();
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public void seekTo(int position) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(position);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public int getDuration() {
        try {
            return mediaPlayer != null ? mediaPlayer.getDuration() : 0;
        } catch (IllegalStateException e) {
            return 0;
        }
    }

    public int getCurrentPosition() {
        try {
            return mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0;
        } catch (IllegalStateException e) {
            return 0;
        }
    }

    public void releasePlayer() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            } catch (IllegalStateException e) {
                // Ignore
            }

            mediaPlayer.release();
            mediaPlayer = null;
            isPrepared.setValue(false);
            isPlaying.setValue(false);
        }
    }
}
