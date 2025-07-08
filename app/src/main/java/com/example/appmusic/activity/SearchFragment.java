package com.example.appmusic.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmusic.R;
import com.example.appmusic.adapter.ArtistAdapter;
import com.example.appmusic.adapter.GenreAdapter;
import com.example.appmusic.adapter.SearchAdapter;
import com.example.appmusic.databinding.FragmentSearchBinding;
import com.example.appmusic.model.Genre;
import com.example.appmusic.model.Song;
import com.example.appmusic.repository.GenreRepository;
import com.example.appmusic.viewmodel.ArtistViewModel;
import com.example.appmusic.viewmodel.SongViewModel;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SongViewModel songViewModel;
    private SearchAdapter searchAdapter;
    private RecyclerView recyclerArtist;
    private GenreAdapter genreAdapter;
    private RecyclerView recyclerGenre;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        songViewModel = new ViewModelProvider(this).get(SongViewModel.class);

        // Khởi tạo adapter
        searchAdapter = new SearchAdapter(requireContext(), position -> {
            Song clickedSong = searchAdapter.getSongAt(position); // ✅ đúng cú pháp
            if (clickedSong != null) {
                int songId = clickedSong.getId();
                Bundle bundle = new Bundle();
                bundle.putInt("songId", songId);
                NavController navController = NavHostFragment.findNavController(this);
                navController.navigate(R.id.nowPlayingFragment, bundle);
            }
        });

        // Setup RecyclerView
        binding.recyclerSuggestions.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerSuggestions.setAdapter(searchAdapter);

        // Gõ để tìm kiếm bài hát theo tên
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                if (!keyword.isEmpty()) {
                    binding.recyclerSuggestions.setVisibility(View.VISIBLE);
                    songViewModel.searchSongsByTitle(keyword).observe(getViewLifecycleOwner(), songs -> {
                        searchAdapter.updateSongs(songs);
                    });
                } else {
                    searchAdapter.updateSongs(new ArrayList<>());
                }
            }

            @Override public void afterTextChanged(Editable s) {}
        });



        // ==== 3. Hiển thị danh sách nghệ sĩ (artist list) ====
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

        // ==== 4. Hiển thị danh sách thể loại (genre) ====
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
