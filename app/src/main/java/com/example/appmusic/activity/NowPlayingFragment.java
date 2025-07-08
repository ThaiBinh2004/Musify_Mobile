package com.example.appmusic.activity;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.appmusic.R;
import com.example.appmusic.databinding.FragmentNowPlayingBinding;
import com.example.appmusic.model.Song;
import com.example.appmusic.viewmodel.FavoriteViewModel;
import com.example.appmusic.viewmodel.HistoryViewModel;
import com.example.appmusic.viewmodel.NowPlayingViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class NowPlayingFragment extends Fragment {

    private FragmentNowPlayingBinding binding;
    private NowPlayingViewModel viewModel;
    private FavoriteViewModel favoriteViewModel;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNowPlayingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HistoryViewModel historyViewModel = new ViewModelProvider(
                requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(HistoryViewModel.class);

        viewModel = new ViewModelProvider(requireActivity()).get(NowPlayingViewModel.class);
        favoriteViewModel = new ViewModelProvider(requireActivity()).get(FavoriteViewModel.class);
        viewModel.setAssetManager(requireContext().getAssets());

        int songId = NowPlayingFragmentArgs.fromBundle(getArguments()).getSongId();
        viewModel.playSongById(songId);

        viewModel.isPrepared().observe(getViewLifecycleOwner(), prepared -> {
            if (prepared != null && prepared) {
                startSeekBarUpdate();
                binding.btnPlayPause.setImageResource(R.drawable.ic_pause);
            }
        });

        viewModel.getCurrentSong().observe(getViewLifecycleOwner(), song -> {
            if (song != null) {
                historyViewModel.addToHistory(song.getId());
                binding.tvSongTitle.setText(song.getTitle());
                binding.tvArtist.setText(song.getArtistName());

                try {
                    String assetPath = "ImgSongs/" + song.getImagePath();
                    InputStream inputStream = requireContext().getAssets().open(assetPath);
                    Drawable drawable = Drawable.createFromStream(inputStream, null);
                    binding.imgCover.setImageDrawable(drawable);
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    binding.imgCover.setImageResource(R.drawable.default_img);
                }

                boolean isFav = favoriteViewModel.isFavorite(song.getId());
                binding.btnFavorite.setImageResource(isFav ? R.drawable.ic_heart_filled : R.drawable.favor);
            }
        });

        binding.btnPlayPause.setOnClickListener(v -> {
            viewModel.togglePlayPause();
            binding.btnPlayPause.setImageResource(viewModel.isPlaying() ? R.drawable.ic_pause : R.drawable.play);
        });

        binding.btnNext.setOnClickListener(v -> {
            Integer index = viewModel.getCurrentIndex().getValue();
            List<Song> songs = viewModel.getSongList().getValue();
            if (songs != null && index != null && !songs.isEmpty()) {
                int nextIndex = (index + 1) % songs.size();
                viewModel.playSongById(songs.get(nextIndex).getId());
            }
        });

        binding.btnPrev.setOnClickListener(v -> {
            Integer index = viewModel.getCurrentIndex().getValue();
            List<Song> songs = viewModel.getSongList().getValue();
            if (songs != null && index != null && !songs.isEmpty()) {
                int prevIndex = (index - 1 + songs.size()) % songs.size();
                viewModel.playSongById(songs.get(prevIndex).getId());
            }
        });

        binding.btnShuffle.setOnClickListener(v -> {
            viewModel.toggleShuffle();
            binding.btnShuffle.setImageResource(viewModel.isShuffle() ? R.drawable.ic_shuffle_on : R.drawable.ic_shuffle_off);
        });

        binding.btnRepeat.setOnClickListener(v -> {
            viewModel.toggleRepeat();
            binding.btnRepeat.setImageResource(viewModel.isRepeat() ? R.drawable.ic_repeat_on : R.drawable.ic_repeat_off);
        });

        favoriteViewModel.setUserId(1);
        binding.btnFavorite.setOnClickListener(v -> {
            Song current = viewModel.getCurrentSong().getValue();
            if (current != null) {
                favoriteViewModel.toggleFavorite(current.getId());
                boolean isFav = favoriteViewModel.isFavorite(current.getId());
                binding.btnFavorite.setImageResource(isFav ? R.drawable.ic_heart_filled : R.drawable.favor);
            }
        });

        binding.btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        binding.seekBar.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    viewModel.seekTo(progress);
                    binding.tvCurrentTime.setText(formatTime(progress));
                }
            }
            @Override public void onStartTrackingTouch(android.widget.SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(android.widget.SeekBar seekBar) {}
        });
    }

    private void startSeekBarUpdate() {
        MediaPlayer mediaPlayer = viewModel.getMediaPlayer();
        if (mediaPlayer == null) return;

        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                try {
                    if (mediaPlayer != null && mediaPlayer.isPlaying() && binding != null) {
                        int duration = mediaPlayer.getDuration();
                        int current = mediaPlayer.getCurrentPosition();

                        binding.seekBar.setMax(duration);
                        binding.seekBar.setProgress(current);
                        binding.tvCurrentTime.setText(formatTime(current));
                        binding.tvTotalTime.setText(formatTime(duration));

                        handler.postDelayed(this, 500);
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace(); // tránh crash nếu MediaPlayer đã bị release
                }
            }
        };
        handler.postDelayed(updateSeekBar, 500); // delay 500ms cho MediaPlayer kịp prepare
    }



    private String formatTime(int millis) {
        int minutes = (millis / 1000) / 60;
        int seconds = (millis / 1000) % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (updateSeekBar != null) {
            handler.removeCallbacks(updateSeekBar);
        }
        binding = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (updateSeekBar != null) {
            handler.removeCallbacks(updateSeekBar);
        }
    }
}
