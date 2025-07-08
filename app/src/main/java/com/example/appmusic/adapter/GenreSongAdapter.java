package com.example.appmusic.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
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

public class GenreSongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_SONG = 1;

    private final Context context;
    private List<Song> songList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public GenreSongAdapter(Context context, List<Song> songList, OnItemClickListener listener) {
        this.context = context;
        this.songList = songList;
        this.listener = listener;
    }

    public void updateSongs(List<Song> newSongs) {
        this.songList = newSongs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return songList == null ? 1 : songList.size() + 1; // +1 vì có header
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_SONG;
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
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.tvSongCount.setText(songList.size() + " songs");
        } else {
            int songPosition = position - 1;
            Song song = songList.get(songPosition);
            SongViewHolder songHolder = (SongViewHolder) holder;

            songHolder.tvTitle.setText(song.getTitle());
            songHolder.tvArtist.setText(song.getArtistName());

            String imagePath = "ImgSongs/" + song.getImagePath() ;
            try (InputStream is = context.getAssets().open(imagePath)) {
                Drawable drawable = Drawable.createFromStream(is, null);
                songHolder.imgSong.setImageDrawable(drawable);
            } catch (IOException e) {
                songHolder.imgSong.setImageResource(R.drawable.default_img);
                Log.e("IMG_CHECK", "LỖI khi load ảnh: " + imagePath);

            }

            songHolder.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(songPosition);
            });
        }
    }

    public Song getSongAt(int position) {
        return songList.get(position ); // bỏ qua header
    }

    // ViewHolder for Header
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvSongCount;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSongCount = itemView.findViewById(R.id.tvSongCount);
        }
    }

    // ViewHolder for Song
    static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSong;
        TextView tvTitle, tvArtist;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong = itemView.findViewById(R.id.imgSong);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvArtist = itemView.findViewById(R.id.tvArtist);
        }
    }
}
