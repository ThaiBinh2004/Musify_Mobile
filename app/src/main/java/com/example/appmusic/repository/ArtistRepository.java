package com.example.appmusic.repository;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appmusic.database.MusicDatabaseHelper;
import com.example.appmusic.model.Artist;

import java.util.ArrayList;
import java.util.List;

public class ArtistRepository {
    private MusicDatabaseHelper dbHelper;

    public ArtistRepository(Context context) {
        dbHelper = new MusicDatabaseHelper(context);
    }

    // Thêm nghệ sĩ
    public boolean insertArtist(Artist artist) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", artist.getName());
        values.put("image", artist.getImage());

        long result = db.insert("artists", null, values);
        return result != -1;
    }

    // Lấy tất cả nghệ sĩ
    public List<Artist> getAllArtists() {
        List<Artist> artistList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM artists", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));

                artistList.add(new Artist(id, name, image));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return artistList;
    }

    // Lấy artist theo ID
    public Artist getArtistById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM artists WHERE id = ?", new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
            cursor.close();
            return new Artist(id, name, image);
        }

        cursor.close();
        return null;
    }

    // Cập nhật nghệ sĩ
    public boolean updateArtist(Artist artist) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", artist.getName());
        values.put("image", artist.getImage());

        int rows = db.update("artists", values, "id = ?", new String[]{String.valueOf(artist.getId())});
        return rows > 0;
    }

    // Xoá nghệ sĩ
    public boolean deleteArtist(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("artists", "id = ?", new String[]{String.valueOf(id)});
        return rows > 0;
    }
}

