package com.example.appmusic.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmusic.R;
import com.example.appmusic.model.Song;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_SONG = 1;

    public interface OnSongClickListener {
        void onSongClick(int position);
    }

    private final Context context;
    private List<Song> songs;
    private final OnSongClickListener songClickListener;

    public SongAdapter(Context context, List<Song> songs, OnSongClickListener songClickListener) {
        this.context = context;
        this.songs = songs;
        this.songClickListener = songClickListener;
    }

    public void setSongs(List<Song> newSongs) {
        this.songs = newSongs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return songs != null ? songs.size() + 1 : 1; // 1 header + N songs
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_SONG;
    }

    public Song getSongAt(int position) {
        int songIndex = position - 1;
        if (songs != null && songIndex >= 0 && songIndex < songs.size()) {
            return songs.get(songIndex);
        }
        return null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == VIEW_TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_header_song_list, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_song, parent, false);
            return new SongViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            HeaderViewHolder header = (HeaderViewHolder) holder;
            header.tvSongCount.setText(songs.size() + " songs");
        } else {
            int songPosition = position - 1;
            Song song = songs.get(songPosition);
            SongViewHolder songHolder = (SongViewHolder) holder;

            songHolder.tvTitle.setText(song.getTitle());
            songHolder.tvArtist.setText(song.getArtistName());

            String assetPath = "ImgSongs/" + song.getImagePath() ;
            try (InputStream inputStream = context.getAssets().open(assetPath)) {
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                songHolder.imgSong.setImageDrawable(drawable);
            } catch (IOException e) {
                songHolder.imgSong.setImageResource(R.drawable.default_img);
            }

            songHolder.btnFavorite.setImageResource(
                    song.isFavorite() ? R.drawable.ic_heart_filled : R.drawable.favor
            );

            songHolder.itemView.setOnClickListener(v -> {
                if (songClickListener != null) {
                    songClickListener.onSongClick(songPosition);
                }
            });
        }
    }

    // ViewHolder for Song
    static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSong;
        TextView tvTitle, tvArtist;
        ImageButton btnFavorite;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            imgSong = itemView.findViewById(R.id.imgSong);
        }
    }

    // ViewHolder for Header
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvSongCount;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSongCount = itemView.findViewById(R.id.tvSongCount);
        }
    }
}
