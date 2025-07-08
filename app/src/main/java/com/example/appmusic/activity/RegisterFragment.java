package com.example.appmusic.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.appmusic.R;
import com.example.appmusic.databinding.FragmentRegisterBinding;
import com.example.appmusic.viewmodel.AuthViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private AuthViewModel viewModel;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot(); //
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        NavController navController = Navigation.findNavController(view);

        binding.btnSignUp.setOnClickListener(v -> {
            String username = binding.edtUsername.getText().toString().trim();
            String email = binding.edtEmail.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();
            String confirm = binding.edtConfirmPassword.getText().toString().trim();

            if (!validateInput(username, email, password, confirm)) return;

            String createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            viewModel.register(username, email, password, createdAt);
        });

        viewModel.registerResult.observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(requireContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                navController.navigateUp(); // trở về LoginFragment
            } else {
                Toast.makeText(requireContext(), "Email đã tồn tại", Toast.LENGTH_SHORT).show();
            }
        });

//        binding.txtGoLogin.setOnClickListener(v -> navController.navigateUp());

        binding.txtGoLogin.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_register_to_login);
        });
    }

    private boolean validateInput(String username, String email, String password, String confirm) {
        if (TextUtils.isEmpty(username)) {
            binding.edtUsername.setError("Vui lòng nhập tên người dùng");
            return false;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.setError("Email không hợp lệ");
            return false;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            binding.edtPassword.setError("Mật khẩu ít nhất 6 ký tự");
            return false;
        }
        if (!password.equals(confirm)) {
            binding.edtConfirmPassword.setError("Mật khẩu không khớp");
            return false;
        }
        return true;


    }



}
