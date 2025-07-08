package com.example.appmusic.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.appmusic.R;
import com.example.appmusic.adapter.ArtistSongAdapter;
import com.example.appmusic.databinding.FragmentAllFavouriteBinding;
import com.example.appmusic.model.Song;
import com.example.appmusic.viewmodel.SongViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AllFavouriteFragment extends Fragment {

    private FragmentAllFavouriteBinding binding;
    private ArtistSongAdapter adapter;
    private SongViewModel songViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAllFavouriteBinding.inflate(inflater, container, false);

        setupToolbar();
        setupHeaderImage();

        adapter = new ArtistSongAdapter(requireContext(), new ArrayList<>(), position -> {
            Song clickedSong = adapter.getSongAt(position);
            if (clickedSong != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("songId", clickedSong.getId());
                NavHostFragment.findNavController(this).navigate(R.id.nowPlayingFragment, bundle);
            }
        });

        binding.rvSongList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvSongList.setAdapter(adapter);

        songViewModel = new ViewModelProvider(this).get(SongViewModel.class);
        songViewModel.getFavoriteSongs (1).observe(getViewLifecycleOwner(), songs -> {
            Log.d("FAVOURITE", "Tổng bài hát yêu thích: " + songs.size());
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

    private void setupHeaderImage() {
        try (InputStream is = requireContext().getAssets().open("banner_favour")) {
            Drawable drawable = Drawable.createFromStream(is, null);
            binding.imgFavourBanner.setImageDrawable(drawable);
        } catch (IOException e) {
            binding.imgFavourBanner.setImageResource(R.drawable.music_banner);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
