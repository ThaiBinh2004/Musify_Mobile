package com.example.appmusic.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmusic.R;
import com.example.appmusic.model.Song;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PlaylistSongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private final Context context;
    private List<Song> songList;
    private final OnItemClickListener listener;

    public PlaylistSongAdapter(Context context, List<Song> songList, OnItemClickListener listener) {
        this.context = context;
        this.songList = songList;
        this.listener = listener;
    }

    public void updateSongs(List<Song> newList) {
        this.songList = newList;
        notifyDataSetChanged();
    }

    public Song getSongAt(int position) {
        int realPosition = position - 1;
        return songList != null && realPosition >= 0 && realPosition < songList.size()
                ? songList.get(realPosition) : null;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_header_song_list, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
            return new PlaylistSongViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            int total = songList != null ? songList.size() : 0;
            headerHolder.tvHeader.setText("Tổng số bài hát: " + total);
        } else {
            int realPosition = position - 1;
            Song song = songList.get(realPosition);
            PlaylistSongViewHolder songHolder = (PlaylistSongViewHolder) holder;

            songHolder.tvSongName.setText(song.getTitle());
            songHolder.tvArtistName.setText(song.getArtistName());

            try (InputStream is = context.getAssets().open("ImgSongs/" + song.getImagePath())) {
                Drawable drawable = Drawable.createFromStream(is, null);
                songHolder.imgSong.setImageDrawable(drawable);
            } catch (IOException e) {
                songHolder.imgSong.setImageResource(R.drawable.default_img);
            }

            songHolder.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(realPosition);
            });
        }
    }

    @Override
    public int getItemCount() {
        return (songList != null ? songList.size() : 0) + 1; // +1 cho header
    }

    static class PlaylistSongViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSong;
        TextView tvSongName, tvArtistName;

        public PlaylistSongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong = itemView.findViewById(R.id.imgSong);
            tvSongName = itemView.findViewById(R.id.tvTitle);
            tvArtistName = itemView.findViewById(R.id.tvArtist);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeader;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tvSongCount);
        }
    }
}
