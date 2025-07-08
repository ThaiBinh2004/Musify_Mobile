package com.example.appmusic.repository;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.appmusic.database.MusicDatabaseHelper;
import com.example.appmusic.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistRepository {
    private final MusicDatabaseHelper dbHelper;

    public PlaylistRepository(Context context) {
        dbHelper = new MusicDatabaseHelper(context);
    }

    public long insertPlaylist(Playlist playlist) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", playlist.getName());
        values.put("user_id", playlist.getUserId());
        values.put("image", playlist.getImage());

        long id = db.insert("playlists", null, values);
        if (id == -1) {
            Log.e("DB_ERROR", "INSERT PLAYLIST FAIL: " + values.toString());
        } else {
            Log.d("DB_SUCCESS", "Playlist inserted with id = " + id);
        }
        return id;
    }




    public List<Playlist> getPlaylistsByUser(int userId) {
        List<Playlist> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM playlists WHERE user_id = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                list.add(new Playlist(id, name, userId, image));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean updatePlaylist(Playlist playlist) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", playlist.getName());
        values.put("image", playlist.getImage());
        int rows = db.update("playlists", values, "id = ?", new String[]{String.valueOf(playlist.getId())});
        return rows > 0;


    }

    public boolean deletePlaylist(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("playlists", "id = ?", new String[]{String.valueOf(id)}) > 0;
    }


}
