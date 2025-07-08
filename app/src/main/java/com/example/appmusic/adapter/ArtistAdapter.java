package com.example.appmusic.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appmusic.R;
import com.example.appmusic.model.Artist;
import com.google.android.material.imageview.ShapeableImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private final Context context;
    private List<Artist> artistList;
    private final OnArtistClickListener listener;

    public interface OnArtistClickListener {
        void onArtistClick(Artist artist);
    }

    public ArtistAdapter(Context context, List<Artist> artistList, OnArtistClickListener listener) {
        this.context = context;
        this.artistList = artistList;
        this.listener = listener;
    }

    public void setArtistList(List<Artist> list) {
        this.artistList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.artist_horizontal, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = artistList.get(position);
        holder.tvTitle.setText(artist.getName());

        String imagePath = "ImgArtists/" + artist.getImage() ;
        try (InputStream is = context.getAssets().open(imagePath)) {
            Drawable drawable = Drawable.createFromStream(is, null);
            holder.imgCover.setImageDrawable(drawable);
            Log.d("IMG_CHECK", "ĐÃ LOAD thành công: " + imagePath);
        } catch (IOException e) {
            Log.e("IMG_CHECK", "LỖI khi load ảnh: " + imagePath);
            holder.imgCover.setImageResource(R.drawable.default_img);
        }

        holder.itemView.setOnClickListener(v -> listener.onArtistClick(artist));
    }

    @Override
    public int getItemCount() {
        return artistList != null ? artistList.size() : 0;
    }

    static class ArtistViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imgCover;
        TextView tvTitle;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.imgCover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
