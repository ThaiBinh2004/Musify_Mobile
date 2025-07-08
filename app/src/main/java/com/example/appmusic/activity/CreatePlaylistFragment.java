package com.example.appmusic.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.appmusic.R;
import com.example.appmusic.model.Playlist;
import com.example.appmusic.model.User;
import com.example.appmusic.repository.PlaylistRepository;
import com.example.appmusic.viewmodel.AuthViewModel;

import java.io.IOException;
import java.io.InputStream;

public class CreatePlaylistFragment extends Fragment {


    private EditText edtPlaylistName;
    private ImageView imgPreview;
    private View btnChooseImage, btnCreate;

    private AuthViewModel authViewModel;
    private int userId;

    private String selectedImage = "default_img.png";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        User currentUser = authViewModel.getUserLiveData().getValue();

        if (currentUser != null) {
            userId = currentUser.getId(); // Gán userId
        } else {
            Toast.makeText(requireContext(), "Unknow user", Toast.LENGTH_SHORT).show();
            return;
        }


        edtPlaylistName = view.findViewById(R.id.edtPlaylistName);
        imgPreview = view.findViewById(R.id.imgPreview);
        btnChooseImage = view.findViewById(R.id.btnChooseImage);
        btnCreate = view.findViewById(R.id.btnCreate);

        loadImageFromAssets(selectedImage); // hiển thị ảnh mặc định

        btnChooseImage.setOnClickListener(v -> showImageSelectionDialog());

        btnCreate.setOnClickListener(v -> handleCreatePlaylist());
    }

    private void handleCreatePlaylist() {
        String name = edtPlaylistName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Input your name playlist please", Toast.LENGTH_SHORT).show();
            return;
        }

        // selectedImage luôn là tên ảnh nằm trong assets/ImgPlaylist (ví dụ: "abc.jpg")
        String imagePath = selectedImage; // Lưu tên file ảnh

        Playlist playlist = new Playlist(name, userId, imagePath);

        Log.d("CREATE_PLAYLIST", "Ảnh được chọn từ assets: " + imagePath);

        PlaylistRepository repo = new PlaylistRepository(requireContext());
        long id = repo.insertPlaylist(playlist);
        Log.d("DEBUG", "playlistId mới tạo: " + id);

        if (id > 0) {
            Toast.makeText(requireContext(), "Succes create playlist", Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putLong("playlistId", id); // Truyền ID playlist vừa tạo
            NavHostFragment.findNavController(this).navigate(R.id.action_createPlaylistFragment_to_selectSongsFragment, bundle);

        } else {
            Toast.makeText(requireContext(), "Incorrect create", Toast.LENGTH_SHORT).show();
        }
    }



    private void loadImageFromAssets(String fileName) {
        try (InputStream is = requireContext().getAssets().open("ImgPlaylist/" + fileName)) {
            Drawable drawable = Drawable.createFromStream(is, null);
            imgPreview.setImageDrawable(drawable);
        } catch (IOException e) {
            imgPreview.setImageResource(R.drawable.default_img);
        }
    }

    private void showImageSelectionDialog() {
        try {
            String[] imageFiles = requireContext().getAssets().list("ImgPlaylist");
            if (imageFiles == null || imageFiles.length == 0) {
                Toast.makeText(requireContext(), "No image", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Choose playlist");
            builder.setItems(imageFiles, (dialog, which) -> {
                selectedImage = imageFiles[which];
                loadImageFromAssets(selectedImage);
            });
            builder.show();

        } catch (IOException e) {
            Toast.makeText(requireContext(), "Error read image", Toast.LENGTH_SHORT).show();
        }
    }
}
