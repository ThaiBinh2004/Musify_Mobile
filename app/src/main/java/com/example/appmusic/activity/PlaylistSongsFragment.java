package com.example.appmusic.activity;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.appmusic.R;
import com.example.appmusic.adapter.PlaylistSongAdapter;
import com.example.appmusic.databinding.FragmentArtistSongBinding;
import com.example.appmusic.model.Song;
import com.example.appmusic.repository.PlaylistSongRepository;
import com.example.appmusic.repository.SongRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PlaylistSongsFragment extends Fragment {

    private FragmentArtistSongBinding binding;
    private PlaylistSongAdapter adapter;

    private int playlistId;
    private String playlistName;
    private String playlistImage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentArtistSongBinding.inflate(inflater, container, false);

        // Nhận bundle
        Bundle args = getArguments();
        if (args != null) {
            playlistId = args.getInt("playlistId");
            playlistName = args.getString("playlistName");
            playlistImage = args.getString("playlistImage");
            Log.d("DEBUG_BUNDLE", "playlistId=" + playlistId +
                    ", name=" + playlistName + ", image=" + playlistImage);
        }

        setupToolbar();
        displayPlaylistInfo();

        // Adapter bài hát trong playlist
        adapter = new PlaylistSongAdapter(requireContext(), new ArrayList<>(), position -> {
            Song clickedSong = adapter.getSongAt(position);
            if (clickedSong != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("songId", clickedSong.getId());
                NavHostFragment.findNavController(this).navigate(R.id.nowPlayingFragment, bundle);
            }
        });

        binding.recyclerArtistSongs.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerArtistSongs.setAdapter(adapter);

        loadSongsFromPlaylist();

        return binding.getRoot();
    }

    private void loadSongsFromPlaylist() {
        PlaylistSongRepository playlistSongRepo = new PlaylistSongRepository(requireContext());
        SongRepository songRepo = new SongRepository(requireContext());

        // Lấy songId trong playlist
        List<Integer> songIds = new ArrayList<>();
        playlistSongRepo.getSongsByPlaylist(playlistId).forEach(ps -> songIds.add(ps.getSongId()));

        // Lấy danh sách bài hát tương ứng
        List<Song> allSongs = songRepo.getAllSongs();
        List<Song> songsInPlaylist = new ArrayList<>();

        for (Song s : allSongs) {
            if (songIds.contains(s.getId())) {
                songsInPlaylist.add(s);
            }
        }

        for (int i = 0; i < songsInPlaylist.size(); i++) {
            Log.d("PLAYLIST_SONG", "i=" + i + ", id=" + songsInPlaylist.get(i).getId() + ", title=" + songsInPlaylist.get(i).getTitle());
        }

        Log.d("PLAYLIST_DETAIL", "Số bài hát trong playlist: " + songsInPlaylist.size());
        adapter.updateSongs(songsInPlaylist); // nếu adapter có phương thức này
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

    private void displayPlaylistInfo() {
        binding.tvArtistName.setText(playlistName);
        if (playlistImage != null) {
            try (InputStream is = requireContext().getAssets().open("ImgPlaylist/" + playlistImage)) {
                Drawable drawable = Drawable.createFromStream(is, null);
                binding.imgArtistBanner.setImageDrawable(drawable);
                Log.d("PLAYLIST_IMG", "Đường dẫn ảnh: " + playlistImage);
            } catch (IOException e) {
                Log.e("PLAYLIST_IMG", "Không tìm thấy ảnh: " + playlistImage);
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
