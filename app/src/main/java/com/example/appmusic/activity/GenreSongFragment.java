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
import com.example.appmusic.adapter.GenreSongAdapter;
import com.example.appmusic.databinding.FragmentGenreSongBinding;
import com.example.appmusic.model.Song;
import com.example.appmusic.viewmodel.SongViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GenreSongFragment extends Fragment {

    private FragmentGenreSongBinding binding;
    private GenreSongAdapter adapter;
    private SongViewModel songViewModel;

    private int genreId;
    private String genreName;
    private String genreImage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGenreSongBinding.inflate(inflater, container, false);

        // Nhận dữ liệu
        Bundle args = getArguments();
        if (args != null) {
            genreId = args.getInt("genreId");
            genreName = args.getString("genreName");
            genreImage = args.getString("genreImage");
        }

        setupToolbar();
        displayGenreInfo();

        adapter = new GenreSongAdapter(requireContext(), new ArrayList<>(), position -> {
            Song clickedSong = adapter.getSongAt(position);
            if (clickedSong != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("songId", clickedSong.getId());
                NavHostFragment.findNavController(this).navigate(R.id.nowPlayingFragment, bundle);
            }
        });

        binding.recyclerGenreSongs.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerGenreSongs.setAdapter(adapter);

        songViewModel = new ViewModelProvider(this).get(SongViewModel.class);
        songViewModel.getSongsByGenreId(genreId).observe(getViewLifecycleOwner(), songs -> {
            Log.d("GENRE_DETAIL", "Số bài hát: " + songs.size());
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

    private void displayGenreInfo() {
        binding.tvGenreName.setText(genreName);
        if (genreImage != null) {
            try (InputStream is = requireContext().getAssets().open("ImgGenres/" + genreImage)) {
                Drawable drawable = Drawable.createFromStream(is, null);
                binding.imgGenreBanner.setImageDrawable(drawable);
            } catch (IOException e) {
                binding.imgGenreBanner.setImageResource(R.drawable.default_img);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
