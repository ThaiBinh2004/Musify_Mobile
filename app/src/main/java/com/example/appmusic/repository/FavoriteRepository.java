package com.example.appmusic.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appmusic.database.MusicDatabaseHelper;
import com.example.appmusic.model.Favorite;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRepository {
    private final MusicDatabaseHelper dbHelper;

    public FavoriteRepository(Context context) {
        dbHelper = new MusicDatabaseHelper(context);
    }

    public boolean insertFavorite(Favorite favorite) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", favorite.getUserId());
        values.put("song_id", favorite.getSongId());
        long result = db.insert("favorites", null, values);
        return result != -1;
    }

    public List<Favorite> getFavoritesByUser(int userId) {
        List<Favorite> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM favorites WHERE user_id = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int songId = cursor.getInt(cursor.getColumnIndexOrThrow("song_id"));
                list.add(new Favorite(id, userId, songId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean deleteFavorite(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("favorites", "id = ?", new String[]{String.valueOf(id)}) > 0;
    }
}
