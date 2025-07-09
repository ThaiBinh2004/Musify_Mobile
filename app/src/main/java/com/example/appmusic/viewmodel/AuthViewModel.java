package com.example.appmusic.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.appmusic.model.User;
import com.example.appmusic.repository.UserRepository;

public class AuthViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    public MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> registerResult = new MutableLiveData<>();
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    // Đăng nhập
    public void login(String email, String password) {
        boolean success = userRepository.login(email, password);
        loginResult.setValue(success);

        if (success) {
            User user = userRepository.getUserByEmail(email);
            userLiveData.setValue(user);

            //  Lưu user_id vào SharedPreferences
            getApplication()
                    .getSharedPreferences("user_session", Context.MODE_PRIVATE)
                    .edit()
                    .putInt("user_id", user.getId())
                    .apply();
        }
    }


    // Đăng ký
    public void register(String username, String email, String password, String createdAt) {
        if (userRepository.isUserExists(email)) {
            registerResult.setValue(false); // Email đã tồn tại
        } else {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setCreatedAt(createdAt);

            boolean success = userRepository.insertUser(newUser);
            registerResult.setValue(success);

            if (success) {
                userLiveData.setValue(newUser); // Đăng ký xong set luôn user
            }
        }
    }

    //  Cho phép Fragment truy cập user hiện tại
    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    //  Gọi khi cập nhật thông tin
    public boolean updateUser(User user) {
        boolean success = userRepository.updateUser(user);
        if (success) {
            userLiveData.setValue(user); // Cập nhật lại UI
        }
        return success;
    }

    //  Gọi lại nếu cần reload user từ email
    public void loadUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);
        userLiveData.setValue(user);
    }

    public void logout() {
        // Xoá SharedPreferences để xoá session
        getApplication()
                .getSharedPreferences("user_session", Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        userLiveData.setValue(null); // <- Bắt buộc
    }

    public void resetLoginResult() {
        loginResult.setValue(null);
    }





}
