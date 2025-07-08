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
import com.example.appmusic.model.Playlist;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PlaylistManageAdapter extends RecyclerView.Adapter<PlaylistManageAdapter.ViewHolder> {

    public interface OnPlaylistActionListener {
        void onDeleteClick(Playlist playlist);
        void onUpdateClick(Playlist playlist);
    }

    private final Context context;
    private List<Playlist> playlistList;
    private final OnPlaylistActionListener listener;

    public PlaylistManageAdapter(Context context, List<Playlist> playlistList, OnPlaylistActionListener listener) {
        this.context = context;
        this.playlistList = playlistList;
        this.listener = listener;
    }

    public void updateData(List<Playlist> newList) {
        this.playlistList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manage_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlistList.get(position);
        holder.tvName.setText(playlist.getName());

        // Load ảnh từ assets
        try (InputStream is = context.getAssets().open("ImgPlaylist/" + playlist.getImage())) {
            Drawable drawable = Drawable.createFromStream(is, null);
            holder.imgCover.setImageDrawable(drawable);
        } catch (IOException e) {
            holder.imgCover.setImageResource(R.drawable.default_img);
        }

        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(playlist));
        holder.btnUpdate.setOnClickListener(v -> listener.onUpdateClick(playlist));
    }

    @Override
    public int getItemCount() {
        return playlistList != null ? playlistList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover, btnDelete, btnUpdate;
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.imgCover);
            tvName = itemView.findViewById(R.id.tvName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }
    }
}
