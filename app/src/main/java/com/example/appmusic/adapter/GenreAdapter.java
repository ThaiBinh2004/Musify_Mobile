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
import com.example.appmusic.model.Genre;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    private final Context context;
    private List<Genre> genreList;
    private final OnGenreClickListener listener;

    public interface OnGenreClickListener {
        void onGenreClick(int position);
    }

    public GenreAdapter(Context context, List<Genre> genreList, OnGenreClickListener listener) {
        this.context = context;
        this.genreList = genreList;
        this.listener = listener;
    }

    public void setGenres(List<Genre> newList) {
        this.genreList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_genre_grid, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        Genre genre = genreList.get(position);
        holder.tvGenreName.setText(genre.getName());

        // Load ảnh từ assets
        String imagePath = "ImgGenres/" + genre.getImage() ;
        try (InputStream is = context.getAssets().open(imagePath)) {
            Drawable drawable = Drawable.createFromStream(is, null);
            holder.imgGenre.setImageDrawable(drawable);
        } catch (IOException e) {
            holder.imgGenre.setImageResource(R.drawable.banner_all_music);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onGenreClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return genreList != null ? genreList.size() : 0;
    }

    public Genre getGenreAt(int position) {
        return genreList.get(position);
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGenre;
        TextView tvGenreName;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGenre = itemView.findViewById(R.id.imgGenre);
            tvGenreName = itemView.findViewById(R.id.tvGenreName);
        }
    }
}
