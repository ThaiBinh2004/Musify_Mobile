package com.example.appmusic.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmusic.R;
import com.example.appmusic.adapter.ArtistAdapter;
import com.example.appmusic.adapter.ArtistSongAdapter;
import com.example.appmusic.adapter.GenreAdapter;
import com.example.appmusic.adapter.HistoryAdapter;
import com.example.appmusic.adapter.PlaylistAdapter;
import com.example.appmusic.adapter.RecommendationAdapter;
import com.example.appmusic.adapter.SongAdapter;
import com.example.appmusic.model.Genre;
import com.example.appmusic.model.Playlist;
import com.example.appmusic.model.Song;
import com.example.appmusic.model.User;
import com.example.appmusic.repository.GenreRepository;
import com.example.appmusic.repository.PlaylistRepository;
import com.example.appmusic.viewmodel.ArtistViewModel;
import com.example.appmusic.viewmodel.AuthViewModel;
import com.example.appmusic.viewmodel.HistoryViewModel;
import com.example.appmusic.viewmodel.PlaylistViewModel;
import com.example.appmusic.viewmodel.RecommendationViewModel;
import com.example.appmusic.viewmodel.SongViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private PlaylistViewModel playlistViewModel;
    private AuthViewModel authViewModel;

    private RecyclerView recyclerRecent;
    private RecyclerView recyclerRecommendation;
    private RecyclerView recyclerArtist;
    private RecyclerView recyclerGenre;
    private GenreAdapter genreAdapter;
    private  RecyclerView recyclerPlaylists;
    private HistoryAdapter historyAdapter;
    private RecommendationAdapter recommendationAdapter;
    private ArtistSongAdapter artistSongAdapter;

    private HistoryViewModel historyViewModel;
    private RecommendationViewModel recommendationViewModel;

    private RecyclerView recyclerPlaylist;
    private PlaylistAdapter playlistAdapter;
    private int userId;


    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ImageView imgSearch = view.findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(v -> {
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNavigationView);
            bottomNav.setSelectedItemId(R.id.searchFragment); // chọn tab Search
        });




        // ==== 1. Hiển thị lịch sử phát nhạc ====
        recyclerRecent = view.findViewById(R.id.recyclerRecentlyPlayed);
        recyclerRecent.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        historyAdapter = new HistoryAdapter(requireContext(), new ArrayList<>(), position -> {
            Song song = historyAdapter.getSongAt(position);
            Bundle bundle = new Bundle();
            bundle.putInt("songId", song.getId());
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.nowPlayingFragment, bundle);
        });
        recyclerRecent.setAdapter(historyAdapter);

        historyViewModel = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);
        historyViewModel.getHistorySongs().observe(getViewLifecycleOwner(), songs -> {
            historyAdapter.updateSongs(songs);
        });

        // ==== 2. Hiển thị gợi ý bài hát (recommendation) ====

        recommendationAdapter = new RecommendationAdapter(requireContext(), new ArrayList<>(), new RecommendationAdapter.OnItemClickListener() {
            @Override
            public void onSongClick(int position) {
                Song song = recommendationAdapter.getSongAt(position);
                Bundle bundle = new Bundle();
                bundle.putInt("songId", song.getId());
                NavController navController = NavHostFragment.findNavController(HomeFragment.this);
                navController.navigate(R.id.nowPlayingFragment, bundle);
            }

            @Override
            public void onSeeMoreClick() {
                // Điều hướng tới danh sách gợi ý đầy đủ nếu có
                NavController navController = NavHostFragment.findNavController(HomeFragment.this);
                navController.navigate(R.id.allSongsFragment);
            }
        });


        recyclerRecommendation = view.findViewById(R.id.recyclerTopHits);
        recyclerRecommendation.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerRecommendation.setAdapter(recommendationAdapter);

// Khởi tạo ViewModel và quan sát gợi ý
        recommendationViewModel = new ViewModelProvider(requireActivity()).get(RecommendationViewModel.class);
        recommendationViewModel.getRecommendedSongs().observe(getViewLifecycleOwner(), recommended -> {
            recommendationAdapter.updateSongs(recommended);
        });

        // ==== 3. Hiển thị danh sách playlist ====
        recyclerPlaylists = view.findViewById(R.id.recyclerPlaylists);
        recyclerPlaylists.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        playlistAdapter = new PlaylistAdapter(
                requireContext(),
                new ArrayList<>(),
                position -> {
                    Playlist playlist = playlistAdapter.getPlaylistAt(position); // OK
                    Bundle bundle = new Bundle();
                    bundle.putInt("playlistId", playlist.getId());
                    bundle.putString("playlistName", playlist.getName());
                    bundle.putString("playlistImage", playlist.getImage());
                    NavController navController = NavHostFragment.findNavController(this);
                    navController.navigate(R.id.playlistSongsFragment, bundle);
                }
        );
        recyclerPlaylists.setAdapter(playlistAdapter);

        PlaylistRepository playlistRepository = new PlaylistRepository(requireContext());
        int currentUserId = 1;
        List<Playlist> playlists = playlistRepository.getPlaylistsByUser(currentUserId);
        playlistAdapter.updatePlaylists(playlists);




// ==== 4. Hiển thị danh sách nghệ sĩ (artist list) ====
        recyclerArtist = view.findViewById(R.id.recyclerArtist);
        recyclerArtist.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        ArtistAdapter artistAdapter = new ArtistAdapter(
                requireContext(),
                new ArrayList<>(),
                artist -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("artistId", artist.getId());
                    bundle.putString("artistName", artist.getName());
                    bundle.putString("artistImage", artist.getImage());
                    NavController navController = NavHostFragment.findNavController(this);
                    navController.navigate(R.id.artistSongsFragment, bundle); // điều hướng đến Artist Detail
                }
        );
        recyclerArtist.setAdapter(artistAdapter);

// Gọi ViewModel
        ArtistViewModel artistViewModel = new ViewModelProvider(requireActivity()).get(ArtistViewModel.class);
        artistViewModel.getArtistList().observe(getViewLifecycleOwner(), artists -> {
            artistAdapter.setArtistList(artists);
        });

        // ==== 5. Hiển thị danh sách thể loại (genre) ====
        recyclerGenre = view.findViewById(R.id.recyclerGenres);
        recyclerGenre.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        genreAdapter = new GenreAdapter(requireContext(), new ArrayList<>(), position -> {
            Genre genre = genreAdapter.getGenreAt(position);
            Bundle bundle = new Bundle();
            bundle.putInt("genreId", genre.getId());
            bundle.putString("genreName", genre.getName());
            bundle.putString("genreImage", genre.getImage());
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.genreSongsFragment, bundle);
        });
        recyclerGenre.setAdapter(genreAdapter);

// Lấy genre từ ViewModel hoặc Repository thủ công
        GenreRepository genreRepository = new GenreRepository(requireContext());
        genreAdapter.setGenres(genreRepository.getAllGenres());



    }

}

