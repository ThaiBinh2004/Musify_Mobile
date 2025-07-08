package com.example.appmusic.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.appmusic.R;
import com.example.appmusic.model.Song;
import com.example.appmusic.viewmodel.NowPlayingViewModel;

import java.io.IOException;
import java.io.InputStream;

public class MiniPlayerFragment extends Fragment {

    private NowPlayingViewModel nowPlayingViewModel;
    private ImageView imgThumbnail, btnPlayPause, btnClose;
    private TextView txtTitle, txtArtist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mini_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgThumbnail = view.findViewById(R.id.imgThumbnail);
        btnPlayPause = view.findViewById(R.id.btnPlayPause);
        btnClose = view.findViewById(R.id.btnClose);
        txtTitle = view.findViewById(R.id.txtSongTitle);
        txtArtist = view.findViewById(R.id.txtArtist);

        View parentView = getView();
        nowPlayingViewModel.getCurrentSong().observe(getViewLifecycleOwner(), song -> {
            if (song != null) {
                txtTitle.setText(song.getTitle());
                txtArtist.setText(song.getArtistName());

                // Tự hiện MiniPlayer nếu đang bị ẩn
                if (parentView != null && parentView.getVisibility() != View.VISIBLE) {
                    parentView.setVisibility(View.VISIBLE);
                }

                // Load ảnh bài hát nếu có
                try {
                    InputStream inputStream = requireContext().getAssets().open(song.getImagePath());
                    Drawable drawable = Drawable.createFromStream(inputStream, null);
                    imgThumbnail.setImageDrawable(drawable);
                } catch (IOException e) {
                    imgThumbnail.setImageResource(R.drawable.default_img); // fallback
                }
            } else {
                if (parentView != null) {
                    parentView.setVisibility(View.GONE); // Ẩn khi không có bài nào
                }
            }
        });


        nowPlayingViewModel = new ViewModelProvider(requireActivity()).get(NowPlayingViewModel.class);

        nowPlayingViewModel.getCurrentSong().observe(getViewLifecycleOwner(), song -> {
            if (song != null) {
                txtTitle.setText(song.getTitle());
                txtArtist.setText(song.getArtistName());
                // TODO: Load ảnh nếu có
            }
        });

        nowPlayingViewModel.isPrepared().observe(getViewLifecycleOwner(), prepared -> {
            if (prepared != null && prepared) {
                btnPlayPause.setImageResource(nowPlayingViewModel.isPlaying() ? R.drawable.ic_pause : R.drawable.play);
            }
        });

        nowPlayingViewModel.getCurrentSong().observe(getViewLifecycleOwner(), song -> {
            if (song != null) {
                requireView().setVisibility(View.VISIBLE);
                txtTitle.setText(song.getTitle());
                txtArtist.setText(song.getArtistName());
            } else {
                requireView().setVisibility(View.GONE);
            }
        });


        btnPlayPause.setOnClickListener(v -> nowPlayingViewModel.togglePlayPause());

        btnClose.setOnClickListener(v -> nowPlayingViewModel.releasePlayer());

        view.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.nowPlayingFragment));
    }

}
