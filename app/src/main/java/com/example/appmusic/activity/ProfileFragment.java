package com.example.appmusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.appmusic.R;
import com.example.appmusic.model.Song;
import com.example.appmusic.model.User;
import com.example.appmusic.repository.PlaylistRepository;
import com.example.appmusic.repository.SongRepository;
import com.example.appmusic.viewmodel.AuthViewModel;

import java.util.HashSet;
import java.util.Set;

public class ProfileFragment extends Fragment {

    private AuthViewModel authViewModel;
    private TextView txtUsername, txtEmail;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ view
        txtUsername = view.findViewById(R.id.txtUsername);
        txtEmail = view.findViewById(R.id.txtEmail);
        Button btnLogout = view.findViewById(R.id.btnLogout);
        Button btnCreatePlaylist = view.findViewById(R.id.btnCreatePlaylist);
        Button btnManagePlaylist = view.findViewById(R.id.btnManagePlaylist);


        // View đếm
        TextView tvSongCount = view.findViewById(R.id.tvSongCount);
        TextView tvPlaylistCount = view.findViewById(R.id.tvPlaylistCount);
        TextView tvArtistCount = view.findViewById(R.id.tvArtistCount);

        // Khởi tạo ViewModel
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        // Quan sát dữ liệu user
        authViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                txtUsername.setText(user.getUsername());
                txtEmail.setText(user.getEmail());
            }
        });

        // Logout
        btnLogout.setOnClickListener(v -> {
            authViewModel.logout();

            NavHostFragment.findNavController(this).navigate(
                    R.id.splashFragment,
                    null,
                    new NavOptions.Builder()
                            .setPopUpTo(R.id.nav_graph, true) // id của graph gốc
                            .build()
            );
        });


        // Sửa hồ sơ
        view.findViewById(R.id.btnEdit).setOnClickListener(v -> showEditDialog());

        // 👉 Thống kê
        SongRepository songRepo = new SongRepository(requireContext());
        PlaylistRepository playlistRepo = new PlaylistRepository(requireContext());

        // Đếm bài hát
        int songCount = songRepo.getAllSongs().size();
        tvSongCount.setText(songCount + " songs");

        // Đếm playlist
        User currentUser = authViewModel.getUserLiveData().getValue();
        int playlistCount = 0;
        if (currentUser != null) {
            playlistCount = playlistRepo.getPlaylistsByUser(currentUser.getId()).size();
        }
        tvPlaylistCount.setText(playlistCount + " playlists");


        // Đếm nghệ sĩ duy nhất
        Set<String> artistSet = new HashSet<>();
        for (Song s : songRepo.getAllSongs()) {
            artistSet.add(s.getArtistName());
        }
        tvArtistCount.setText(artistSet.size() + " artists");

        // 👉 Nút tạo playlist
        btnCreatePlaylist.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.createPlaylistFragment);
        });

        btnManagePlaylist.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.managePlaylistsFragment); // ⬅️ fragment này bạn tạo sau
        });
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit profile");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null);
        EditText edtUsername = dialogView.findViewById(R.id.edtUsername);
        EditText edtEmail = dialogView.findViewById(R.id.edtEmail);

        User currentUser = authViewModel.getUserLiveData().getValue();
        if (currentUser != null) {
            edtUsername.setText(currentUser.getUsername());
            edtEmail.setText(currentUser.getEmail());
        }

        builder.setView(dialogView);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newUsername = edtUsername.getText().toString().trim();
            String newEmail = edtEmail.getText().toString().trim();

            if (!newUsername.isEmpty() && !newEmail.isEmpty()) {
                currentUser.setUsername(newUsername);
                currentUser.setEmail(newEmail);

                boolean success = authViewModel.updateUser(currentUser);
                if (success) {
                    Toast.makeText(requireContext(), "Update successful", Toast.LENGTH_SHORT).show();
                    authViewModel.loadUserByEmail(newEmail);
                } else {
                    Toast.makeText(requireContext(), "Update failed.\n", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
