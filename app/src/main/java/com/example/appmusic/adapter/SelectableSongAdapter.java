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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectableSongAdapter extends RecyclerView.Adapter<SelectableSongAdapter.SongViewHolder> {
    private final List<Song> allSongs;
    private final Set<Integer> selectedSongIds;

    public SelectableSongAdapter(List<Song> songs, Set<Integer> existingSelectedIds) {
        this.allSongs = songs;
        this.selectedSongIds = new HashSet<>(existingSelectedIds); // copy để tránh trỏ cùng tham chiếu
    }

    public Set<Integer> getSelectedSongIds() {
        return selectedSongIds;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_checkbox, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = allSongs.get(position);
        holder.txtTitle.setText(song.getTitle());

        // Tạm bỏ listener để tránh bị gọi lại khi setChecked
        holder.checkbox.setOnCheckedChangeListener(null);

        // Thiết lập trạng thái check
        boolean isChecked = selectedSongIds.contains(song.getId());
        holder.checkbox.setChecked(isChecked);

        // Lắng nghe thay đổi khi người dùng tích/bỏ
        holder.checkbox.setOnCheckedChangeListener((buttonView, checked) -> {
            if (checked) {
                selectedSongIds.add(song.getId());
            } else {
                selectedSongIds.remove(song.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return allSongs.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        CheckBox checkbox;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.tvTitle);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }
}
