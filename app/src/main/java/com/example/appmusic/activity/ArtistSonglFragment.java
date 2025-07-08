package com.example.appmusic.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.appmusic.R;
import com.example.appmusic.adapter.ArtistSongAdapter;
import com.example.appmusic.databinding.FragmentArtistSongBinding;
import com.example.appmusic.databinding.FragmentArtistSongBinding;
import com.example.appmusic.model.Song;
import com.example.appmusic.viewmodel.SongViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ArtistSonglFragment extends Fragment {

    private FragmentArtistSongBinding binding;
    private ArtistSongAdapter adapter;
    private SongViewModel songViewModel;

    private int artistId;
    private String artistName;
    private String artistImage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentArtistSongBinding.inflate(inflater, container, false);

        // Nhận dữ liệu từ Bundle
        Bundle args = getArguments();
        if (args != null) {
            artistId = args.getInt("artistId");
            artistName = args.getString("artistName");
            artistImage = args.getString("artistImage");
        }

        setupToolbar();
        displayArtistInfo();

        adapter = new ArtistSongAdapter(requireContext(), new ArrayList<>(), position -> {
            Song clickedSong = adapter.getSongAt(position);
            if (clickedSong != null) {
                int songId = clickedSong.getId(); // ✅ lấy ID
                Bundle bundle = new Bundle();
                bundle.putInt("songId", songId);
                NavHostFragment.findNavController(this).navigate(R.id.nowPlayingFragment, bundle);
            }
        });

        binding.recyclerArtistSongs.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerArtistSongs.setAdapter(adapter);

        songViewModel = new ViewModelProvider(this).get(SongViewModel.class);
        songViewModel.getSongsByArtistId(artistId).observe(getViewLifecycleOwner(), songs -> {
            Log.d("ARTIST_DETAIL", "Số bài hát: " + songs.size());
            adapter.updateSongs(songs);
        });

        return binding.getRoot();
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.toolbar;
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
    }

    private void displayArtistInfo() {
        binding.tvArtistName.setText(artistName);
        if (artistImage != null) {
            try {
                InputStream is = requireContext().getAssets().open("ImgArtists/" + artistImage);
                Drawable drawable = Drawable.createFromStream(is, null);
                binding.imgArtistBanner.setImageDrawable(drawable);
                Log.d("IMG_CHECK", "ĐÃ LOAD thành công: " + is);

                is.close();
            } catch (IOException e) {
                binding.imgArtistBanner.setImageResource(R.drawable.default_img);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
