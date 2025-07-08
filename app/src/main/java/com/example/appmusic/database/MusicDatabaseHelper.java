package com.example.appmusic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MusicDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "music_app.db";
    private static final int DATABASE_VERSION = 1;

    public MusicDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Bảng người dùng
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "email TEXT UNIQUE," +
                "password TEXT," +
                "created_at TEXT)");

        // Các bảng khác giữ nguyên
        db.execSQL("CREATE TABLE genres (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "image TEXT,"+
                "name TEXT)");

        db.execSQL("CREATE TABLE artists (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "image TEXT)");


        db.execSQL("CREATE TABLE songs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "duration INTEGER," +
                "filepath TEXT," +
                "genre_id INTEGER," +
                "artist_id INTEGER," +
                "image_path TEXT,"+
                "FOREIGN KEY(genre_id) REFERENCES genres(id)," +
                "FOREIGN KEY(artist_id) REFERENCES artists(id))");

        db.execSQL("CREATE TABLE favorites (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "song_id INTEGER," +
                "FOREIGN KEY(user_id) REFERENCES users(id)," +
                "FOREIGN KEY(song_id) REFERENCES songs(id))");

        db.execSQL("CREATE TABLE playlists (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "user_id INTEGER," +
                "image TEXT,"+
                "FOREIGN KEY(user_id) REFERENCES users(id))");

        db.execSQL("CREATE TABLE playlist_songs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "playlist_id INTEGER," +
                "song_id INTEGER," +
                "FOREIGN KEY(playlist_id) REFERENCES playlists(id)," +
                "FOREIGN KEY(song_id) REFERENCES songs(id))");

        db.execSQL("CREATE TABLE history (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "song_id INTEGER," +
                "played_at TEXT," +
                "FOREIGN KEY(song_id) REFERENCES songs(id))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS history");
        db.execSQL("DROP TABLE IF EXISTS playlist_songs");
        db.execSQL("DROP TABLE IF EXISTS playlists");
        db.execSQL("DROP TABLE IF EXISTS favorites");
        db.execSQL("DROP TABLE IF EXISTS songs");
        db.execSQL("DROP TABLE IF EXISTS artists");
        db.execSQL("DROP TABLE IF EXISTS genres");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

}
