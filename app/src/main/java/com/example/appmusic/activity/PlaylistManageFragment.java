package com.example.appmusic.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appmusic.R;
import com.example.appmusic.adapter.PlaylistManageAdapter;
import com.example.appmusic.model.Playlist;
import com.example.appmusic.model.User;
import com.example.appmusic.repository.PlaylistRepository;
import com.example.appmusic.viewmodel.AuthViewModel;

import java.util.List;

public class PlaylistManageFragment extends Fragment implements PlaylistManageAdapter.OnPlaylistActionListener {

    private RecyclerView recyclerView;
    private PlaylistManageAdapter adapter;
    private PlaylistRepository playlistRepo;
    private AuthViewModel authViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlist_manage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerViewManage);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        playlistRepo = new PlaylistRepository(requireContext());

        User user = authViewModel.getUserLiveData().getValue();
        if (user != null) {
            List<Playlist> playlists = playlistRepo.getPlaylistsByUser(user.getId());
            adapter = new PlaylistManageAdapter(requireContext(), playlists, this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onDeleteClick(Playlist playlist) {
        playlistRepo.deletePlaylist(playlist.getId());
        List<Playlist> updatedList = playlistRepo.getPlaylistsByUser(authViewModel.getUserLiveData().getValue().getId());
        adapter.updateData(updatedList);
    }

    @Override
    public void onUpdateClick(Playlist playlist) {
        Bundle bundle = new Bundle();
        bundle.putLong("playlistId", playlist.getId());
        NavHostFragment.findNavController(this).navigate(R.id.selectSongsFragment, bundle);
    }
}
