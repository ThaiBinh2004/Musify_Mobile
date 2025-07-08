package com.example.appmusic.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.appmusic.database.MusicDatabaseHelper;
import com.example.appmusic.model.PlaylistSong;

import java.util.ArrayList;
import java.util.List;

public class PlaylistSongRepository {
    private final MusicDatabaseHelper dbHelper;

    public PlaylistSongRepository(Context context) {
        dbHelper = new MusicDatabaseHelper(context);
    }

    public boolean insertPlaylistSong(PlaylistSong playlistSong) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("playlist_id", playlistSong.getPlaylistId());
        values.put("song_id", playlistSong.getSongId());

        long result = db.insert("playlist_songs", null, values);

        if (result == -1) {
            Log.e("DB_ERROR", "Insert playlist_song FAIL: " + values.toString());
        } else {
            Log.d("DB_SUCCESS", "Inserted playlist_song: " + values.toString() + " | ID = " + result);
        }

        return result != -1;
    }


    public List<PlaylistSong> getSongsByPlaylist(int playlistId) {
        List<PlaylistSong> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM playlist_songs WHERE playlist_id = ?", new String[]{String.valueOf(playlistId)});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int songId = cursor.getInt(cursor.getColumnIndexOrThrow("song_id"));
                list.add(new PlaylistSong(id, playlistId, songId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean deletePlaylistSong(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("playlist_songs", "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public void deleteAllSongsInPlaylist(int playlistId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("playlist_songs", "playlist_id = ?", new String[]{String.valueOf(playlistId)});
        db.close();
    }

}

