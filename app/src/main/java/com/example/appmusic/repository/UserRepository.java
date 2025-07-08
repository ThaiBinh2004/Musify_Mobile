package com.example.appmusic.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appmusic.database.MusicDatabaseHelper;
import com.example.appmusic.model.User;

public class UserRepository {
    private final MusicDatabaseHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new MusicDatabaseHelper(context);
    }

    // Thêm người dùng mới
    public boolean insertUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("created_at", user.getCreatedAt());

        long result = db.insert("users", null, values);
        return result != -1;
    }

    // Kiểm tra user tồn tại
    public boolean isUserExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Đăng nhập
    public boolean login(String email, String password) {
        User user = getUserByEmail(email);
        return user != null && user.getPassword().equals(password);
    }

    // Lấy user theo email
    public User getUserByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        String query = "SELECT * FROM users WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow("created_at")));
            cursor.close();
        }

        return user;
    }

    // Cập nhật thông tin người dùng
// Cập nhật tên và email người dùng theo ID
    public boolean updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("email", user.getEmail());

        int result = db.update("users", values, "id = ?", new String[]{String.valueOf(user.getId())});
        return result > 0;
    }


}
