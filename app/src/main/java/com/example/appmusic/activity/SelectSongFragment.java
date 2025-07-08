package com.example.appmusic.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmusic.R;
import com.example.appmusic.adapter.SelectableSongAdapter;
import com.example.appmusic.model.PlaylistSong;
import com.example.appmusic.model.Song;
import com.example.appmusic.repository.PlaylistSongRepository;
import com.example.appmusic.repository.SongRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectSongFragment extends Fragment {

    private int playlistId;
    private SelectableSongAdapter adapter;
    private PlaylistSongRepository playlistSongRepo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            playlistId = (int) getArguments().getLong("playlistId", -1);
            Log.d("DEBUG", "playlistId nhận được: " + playlistId);
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        View btnSave = view.findViewById(R.id.btnSave);

        playlistSongRepo = new PlaylistSongRepository(requireContext());
        SongRepository songRepo = new SongRepository(requireContext());

        // Lấy tất cả bài hát
        List<Song> allSongs = songRepo.getAllSongs();

        // Lấy danh sách bài hát đã có trong playlist
        List<PlaylistSong> existingSongs = playlistSongRepo.getSongsByPlaylist(playlistId);
        Set<Integer> existingSongIds = new HashSet<>();
        for (PlaylistSong ps : existingSongs) {
            existingSongIds.add(ps.getSongId());
        }

        // Khởi tạo adapter với danh sách đã được check sẵn
        adapter = new SelectableSongAdapter(allSongs, existingSongIds);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        btnSave.setOnClickListener(v -> saveSelectedSongs());
    }

    private void saveSelectedSongs() {
        Set<Integer> selectedSongIds = adapter.getSelectedSongIds();

        // Xóa tất cả bài hát cũ trong playlist
        playlistSongRepo.deleteAllSongsInPlaylist(playlistId);

        int count = 0;
        for (Integer songId : selectedSongIds) {
            PlaylistSong ps = new PlaylistSong(playlistId, songId);
            boolean success = playlistSongRepo.insertPlaylistSong(ps);
            if (success) count++;
        }

        Toast.makeText(requireContext(), "Updated successfully " + count + " song", Toast.LENGTH_SHORT).show();
        NavHostFragment.findNavController(this).navigateUp();
    }
}
