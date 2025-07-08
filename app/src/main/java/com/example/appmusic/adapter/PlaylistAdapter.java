package com.example.appmusic.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
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

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    public interface OnItemClickListener {
        void onPlaylistClick(int position);
    }



    private final Context context;
    private List<Playlist> playlistList;
    private final OnItemClickListener listener;

    public PlaylistAdapter(Context context, List<Playlist> playlistList, OnItemClickListener listener) {
        this.context = context;
        this.playlistList = playlistList;
        this.listener = listener;
    }

    public void updatePlaylists(List<Playlist> newList) {
        this.playlistList = newList;
        notifyDataSetChanged();
    }



    public Playlist getPlaylistAt(int position) {
        return playlistList != null && position >= 0 && position < playlistList.size() ? playlistList.get(position) : null;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = playlistList.get(position);

        holder.tvPlaylistName.setText(playlist.getName());

        // Load ảnh từ asset
        try (InputStream is = context.getAssets().open("ImgPlaylist/" + playlist.getImage())) {
            Drawable drawable = Drawable.createFromStream(is, null);
            holder.imgPlaylist.setImageDrawable(drawable);
            Log.e("IMG_CHECK_PLAYLIST", " load ảnh: " + is);
        } catch (IOException e) {
            holder.imgPlaylist.setImageResource(R.drawable.default_img);

        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onPlaylistClick(position);
        });
    }

    @Override
    public int getItemCount() {
        Log.d("CHECK_COUNT", "Playlist count = " + (playlistList != null ? playlistList.size() : 0));
        return playlistList != null ? playlistList.size() : 0;
    }

    static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPlaylist;
        TextView tvPlaylistName;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlaylist = itemView.findViewById(R.id.imgPlaylist);
            tvPlaylistName = itemView.findViewById(R.id.tvPlaylistName);
        }
    }
}
