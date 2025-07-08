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

public class RecommendationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SONG = 0;
    private static final int VIEW_TYPE_FOOTER = 1;

    public interface OnItemClickListener {
        void onSongClick(int position);
        void onSeeMoreClick();
    }

    private final Context context;
    private List<Song> songs;
    private final OnItemClickListener listener;

    public RecommendationAdapter(Context context, List<Song> songs, OnItemClickListener listener) {
        this.context = context;
        this.songs = songs;
        this.listener = listener;
    }

    public void updateSongs(List<Song> newSongs) {
        this.songs = newSongs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return songs != null ? songs.size() + 1 : 1; // N bài + 1 footer
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) return VIEW_TYPE_FOOTER;
        return VIEW_TYPE_SONG;
    }

    public Song getSongAt(int position) {
        return songs != null && position >= 0 && position < songs.size() ? songs.get(position) : null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == VIEW_TYPE_FOOTER) {
            View view = inflater.inflate(R.layout.item_footer_see_more, parent, false);
            return new FooterViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_song, parent, false);
            return new SongViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SongViewHolder) {
            Song song = songs.get(position);
            SongViewHolder songHolder = (SongViewHolder) holder;

            songHolder.tvTitle.setText(song.getTitle());
            songHolder.tvArtist.setText(song.getArtistName());
            songHolder.imgSong.setVisibility(View.VISIBLE); // RESET trạng thái rõ ràng


            String imagePath = "ImgSongs/" + song.getImagePath() ;
            try (InputStream is = context.getAssets().open(imagePath)) {
                Drawable drawable = Drawable.createFromStream(is, null);
                songHolder.imgSong.setImageDrawable(drawable);
                Log.e("IMG_CHECK", " khi load ảnh: " + imagePath);

            } catch (IOException e) {
                songHolder.imgSong.setImageResource(R.drawable.default_img);
                Log.e("IMG_CHECK", "LỖI khi load ảnh: " + imagePath);

            }

            songHolder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSongClick(position);
                }
            });

        } else if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).tvSeeMore.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSeeMoreClick();
                }
            });
        }
    }

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

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView tvSeeMore;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSeeMore = itemView.findViewById(R.id.tvSeeMore);
        }
    }
}
