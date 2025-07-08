package com.example.appmusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appmusic.R;
import com.example.appmusic.adapter.SongAdapter;
import com.example.appmusic.databinding.FragmentAllSongsBinding;
import com.example.appmusic.model.Song;
import com.example.appmusic.viewmodel.FavoriteViewModel;
import com.example.appmusic.viewmodel.NowPlayingViewModel;
import com.example.appmusic.viewmodel.SongViewModel;

import java.util.ArrayList;

public class AllSongsFragment extends Fragment {

    private FragmentAllSongsBinding binding;
    private SongAdapter adapter;
    private SongViewModel songViewModel;
    private NowPlayingViewModel nowPlayingViewModel;
    private FavoriteViewModel favoriteViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAllSongsBinding.inflate(inflater, container, false);

        Toolbar toolbar = binding.toolbar; // Hoặc findViewById nếu không dùng ViewBinding
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);

// Hiển thị nút back
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

// Xử lý khi ấn back
        toolbar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });


        nowPlayingViewModel = new ViewModelProvider(requireActivity()).get(NowPlayingViewModel.class);
        nowPlayingViewModel.setAssetManager(requireContext().getAssets());

        adapter = new SongAdapter(
                requireContext(),
                new ArrayList<>(),
                position -> {
                    Song clickedSong = adapter.getSongAt(position+1); //  lấy bài hát tại vị trí đó
                    if (clickedSong != null) {
                        int songId = clickedSong.getId(); //  lấy ID
                        Bundle bundle = new Bundle();
                        bundle.putInt("songId", songId);
                        NavController navController = NavHostFragment.findNavController(this);
                        navController.navigate(R.id.nowPlayingFragment, bundle);
                    }
                }
        );



        Log.d("CURRENT_FRAGMENT", "AllSongsFragment đang hoạt động");


        binding.rvSongList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvSongList.setAdapter(adapter);

        songViewModel = new ViewModelProvider(this).get(SongViewModel.class);
        songViewModel.getSongs().observe(getViewLifecycleOwner(), songs -> {
            adapter.setSongs(songs);

        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
