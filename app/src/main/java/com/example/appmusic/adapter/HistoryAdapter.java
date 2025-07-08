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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final Context context;
    private final List<Song> historySongs;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public HistoryAdapter(Context context, List<Song> historySongs, OnItemClickListener listener) {
        this.context = context;
        this.historySongs = historySongs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_horizontal, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Song song = historySongs.get(position);
        holder.tvTitle.setText(song.getTitle());

        holder.imgCover.setVisibility(View.VISIBLE); // RESET trạng thái rõ ràng


        String assetPath = "ImgSongs/" + song.getImagePath() ;
        try {
            InputStream inputStream = context.getAssets().open(assetPath);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            holder.imgCover.setImageDrawable(drawable);
            inputStream.close();
        } catch (IOException e) {
            holder.imgCover.setImageResource(R.drawable.default_img);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return historySongs != null ? historySongs.size() : 0;
    }

    public void updateSongs(List<Song> newSongs) {
        historySongs.clear();
        historySongs.addAll(newSongs);
        notifyDataSetChanged();
    }

    public Song getSongAt(int position) {
        return historySongs.get(position);
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover;
        TextView tvTitle;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.imgCover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
