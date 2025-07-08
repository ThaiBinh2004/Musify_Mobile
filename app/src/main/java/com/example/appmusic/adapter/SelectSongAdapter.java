package com.example.appmusic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmusic.R;
import com.example.appmusic.model.Song;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectSongAdapter extends RecyclerView.Adapter<SelectSongAdapter.SongViewHolder> {

    private final List<Song> songs;
    private final Set<Integer> selectedSongIds = new HashSet<>();

    public SelectSongAdapter(List<Song> songs) {
        this.songs = songs;
    }

    public List<Song> getSelectedSongs() {
        List<Song> selected = new ArrayList<>();
        for (Song s : songs) {
            if (selectedSongIds.contains(s.getId())) {
                selected.add(s);
            }
        }
        return selected;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_checkbox, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.tvTitle.setText(song.getTitle());
        holder.checkbox.setChecked(selectedSongIds.contains(song.getId()));

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedSongIds.add(song.getId());
            } else {
                selectedSongIds.remove(song.getId());
            }
        });

        holder.itemView.setOnClickListener(v -> holder.checkbox.setChecked(!holder.checkbox.isChecked()));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        CheckBox checkbox;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }
}
