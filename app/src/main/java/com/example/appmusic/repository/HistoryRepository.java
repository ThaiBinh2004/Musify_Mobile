package com.example.appmusic.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appmusic.database.MusicDatabaseHelper;
import com.example.appmusic.model.Song;

import java.util.ArrayList;
import java.util.List;

public class HistoryRepository {
    private final MusicDatabaseHelper dbHelper;
    private static final int MAX_HISTORY_SIZE = 50;

    public HistoryRepository(Context context) {
        dbHelper = new MusicDatabaseHelper(context);
    }

    // ✅ Lấy bài hát mới nhất
    public Song getLastPlayedSong() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Song song = null;

        String query = "SELECT s.* FROM history h " +
                "JOIN songs s ON h.song_id = s.id " +
                "ORDER BY h.played_at DESC LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
            String filepath = cursor.getString(cursor.getColumnIndexOrThrow("filepath"));
            int genreId = cursor.getInt(cursor.getColumnIndexOrThrow("genre_id"));
            int artistId = cursor.getInt(cursor.getColumnIndexOrThrow("artist_id"));
            String image = cursor.getString(cursor.getColumnIndexOrThrow("image_path"));

            song = new Song(id, title, duration, filepath, genreId, artistId,  image);
        }
        cursor.close();
        return song;
    }

    // ✅ Thêm bài mới vào lịch sử, tự động giới hạn số lượng
    public void insertHistory(int songId, String playedAt) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 1. Xóa bài đã có trong lịch sử (nếu trùng)
        db.delete("history", "song_id = ?", new String[]{String.valueOf(songId)});

        // 2. Thêm bài mới với thời gian hiện tại
        ContentValues values = new ContentValues();
        values.put("song_id", songId);
        values.put("played_at", playedAt);
        db.insert("history", null, values);

        // 3. Giới hạn số lượng bài: giữ lại tối đa MAX_HISTORY_SIZE
        Cursor cursor = db.rawQuery("SELECT id FROM history ORDER BY played_at DESC", null);
        if (cursor.getCount() > MAX_HISTORY_SIZE) {
            cursor.move(MAX_HISTORY_SIZE);
            while (!cursor.isAfterLast()) {
                int idToDelete = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                db.delete("history", "id = ?", new String[]{String.valueOf(idToDelete)});
                cursor.moveToNext();
            }
        }
        cursor.close();
    }


    // ✅ Lấy danh sách tất cả bài hát đã nghe gần đây
    public List<Song> getAllHistorySongs() {
        List<Song> historyList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT s.* FROM history h " +
                "JOIN songs s ON h.song_id = s.id " +
                "ORDER BY h.played_at DESC";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                String filepath = cursor.getString(cursor.getColumnIndexOrThrow("filepath"));
                int genreId = cursor.getInt(cursor.getColumnIndexOrThrow("genre_id"));
                int artistId = cursor.getInt(cursor.getColumnIndexOrThrow("artist_id"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image_path"));

                historyList.add(new Song(id, title, duration, filepath, genreId, artistId,  image));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return historyList;
    }
}
