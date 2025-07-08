package com.example.appmusic.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appmusic.MyApplication;
import com.example.appmusic.R;
import com.example.appmusic.database.MusicDatabaseHelper;

public class SplashFragment extends Fragment {
    private MusicDatabaseHelper dbHelper;
    private SQLiteDatabase database;
    public SplashFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);


//        new Handler().postDelayed(() -> {
//            NavController navController = NavHostFragment.findNavController(this);
//            navController.navigate(R.id.action_splash_to_login);
//        }, 10000); // 2s delay

        view.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_splash_to_login);
        });

        return view;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
