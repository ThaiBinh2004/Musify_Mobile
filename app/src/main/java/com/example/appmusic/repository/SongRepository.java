package com.example.appmusic.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appmusic.database.MusicDatabaseHelper;
import com.example.appmusic.model.Song;
import com.example.appmusic.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SongRepository {
    private final MusicDatabaseHelper dbHelper;

    public SongRepository(Context context) {
        dbHelper = new MusicDatabaseHelper(context);
    }



    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT songs.id, songs.title, songs.duration, songs.filepath, songs.image_path, " +
                "artists.name AS artist_name " +
                "FROM songs " +
                "INNER JOIN artists ON songs.artist_id = artists.id";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                String filepath = cursor.getString(cursor.getColumnIndexOrThrow("filepath"));
                String artistName = cursor.getString(cursor.getColumnIndexOrThrow("artist_name"));
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image_path"));

                Song song = new Song(id, title, duration, filepath, artistName, imagePath);
                songs.add(song);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return songs;
    }

    public List<Song> getSongsByArtistId(int artistId) {
        List<Song> songs = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT songs.id, songs.title, songs.duration, songs.filepath, songs.image_path, " +
                "artists.name AS artist_name " +
                "FROM songs " +
                "INNER JOIN artists ON songs.artist_id = artists.id " +
                "WHERE songs.artist_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(artistId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                String filepath = cursor.getString(cursor.getColumnIndexOrThrow("filepath"));
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image_path"));
                String artistName = cursor.getString(cursor.getColumnIndexOrThrow("artist_name"));

                Song song = new Song(id, title, duration, filepath, artistName, imagePath);
                songs.add(song);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return songs;
    }




    public List<Song> getSongsByGenreId(int genreId) {
        List<Song> songs = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT songs.id, songs.title, songs.image_path, artists.name AS artist_name " +
                        "FROM songs " +
                        "INNER JOIN artists ON songs.artist_id = artists.id " +
                        "WHERE songs.genre_id = ?",
                new String[]{String.valueOf(genreId)}
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String artistName = cursor.getString(cursor.getColumnIndexOrThrow("artist_name"));
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image_path"));

                // isFavorite nên xử lý riêng nếu có bảng favorites
                songs.add(new Song(id, title, artistName, imagePath, false)); // false tạm thời
            } while (cursor.moveToNext());
        }

        cursor.close();
        return songs;
    }

    public List<Song> getFavoriteSongs(int userId) {
        List<Song> songs = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT s.id, s.title, s.image_path, a.name AS artist_name " +
                "FROM songs s " +
                "JOIN favorites f ON s.id = f.song_id " +
                "JOIN artists a ON s.artist_id = a.id " +
                "WHERE f.user_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String artistName = cursor.getString(cursor.getColumnIndexOrThrow("artist_name"));
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image_path"));
                boolean isFavorite = true;

                songs.add(new Song(id, title, artistName, imagePath, isFavorite));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return songs;
    }
    public List<Song> searchSongsByTitle(String keyword) {
        List<Song> songs = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return songs;
        }

        String processedKeyword = Utils.removeVietnameseDiacritics(keyword.toLowerCase());

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT s.id, s.title, s.image_path, a.name AS artist_name " +
                        "FROM songs s " +
                        "JOIN artists a ON s.artist_id = a.id",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String cleanTitle = Utils.removeVietnameseDiacritics(title.toLowerCase());

                if (startsWithWord(cleanTitle, processedKeyword)) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image_path"));
                    String artistName = cursor.getString(cursor.getColumnIndexOrThrow("artist_name"));

                    songs.add(new Song(id, title, artistName, imagePath, false));
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        return songs;
    }

    private boolean startsWithWord(String fullText, String keyword) {
        String[] words = fullText.split("\\s+");
        for (String word : words) {
            if (word.startsWith(keyword)) {
                return true;
            }
        }
        return false;
    }

    public boolean insertSong(Song song) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", song.getTitle());
        values.put("duration", song.getDuration());
        values.put("filepath", song.getFilepath());
        values.put("genre_id", song.getGenreId());
        values.put("artist_id", song.getArtistId());
        values.put("image_path", song.getImagePath());


        long result = db.insert("songs", null, values);
        return result != -1;
    }



    public boolean updateSong(Song song) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", song.getTitle());
        values.put("duration", song.getDuration());
        values.put("filepath", song.getFilepath());
        values.put("genre_id", song.getGenreId());
        values.put("artist_id", song.getArtistId());
        values.put("image_path", song.getImagePath());


        int rows = db.update("songs", values, "id = ?", new String[]{String.valueOf(song.getId())});
        return rows > 0;
    }

    public boolean deleteSong(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("songs", "id = ?", new String[]{String.valueOf(id)});
        return rows > 0;
    }
}
