package com.example.appmusic.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appmusic.database.MusicDatabaseHelper;
import com.example.appmusic.model.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreRepository {
    private final MusicDatabaseHelper dbHelper;

    public GenreRepository(Context context) {
        dbHelper = new MusicDatabaseHelper(context);
    }

    public List<Genre> getAllGenres() {
        List<Genre> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM genres", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

                list.add(new Genre(id, image, name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }



    public boolean insertGenre(Genre genre) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", genre.getImage());
        values.put("name", genre.getName());

        return db.insert("genres", null, values) != -1;
    }
}
