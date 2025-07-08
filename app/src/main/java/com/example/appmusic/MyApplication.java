package com.example.appmusic;

import android.app.Application;
import android.util.Log;

import com.example.appmusic.database.MusicDatabaseHelper;

public class MyApplication extends Application {
    private static MusicDatabaseHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
//        dbHelper = new MusicDatabaseHelper(getApplicationContext());
//        dbHelper.getWritableDatabase();
//        Log.d("MyApplication", "Database initialized");

    }

    public static MusicDatabaseHelper getDbHelper() {
        return dbHelper;
    }
}
