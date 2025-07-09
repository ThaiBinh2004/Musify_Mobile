package com.example.appmusic.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.appmusic.R;
import com.example.appmusic.databinding.FragmentLoginBinding;
import com.example.appmusic.viewmodel.AuthViewModel;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private AuthViewModel viewModel;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString();
            String password = binding.edtPassword.getText().toString();
            viewModel.login(email, password);
        });

        viewModel.loginResult.observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                Toast.makeText(requireContext(), "Successful login", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.homeFragment);
                viewModel.resetLoginResult(); // Thêm dòng này
            } else if (success != null) {
                Toast.makeText(requireContext(), "Incorrect username or password", Toast.LENGTH_SHORT).show();
                viewModel.resetLoginResult();
            }
        });



        // Xử lý dòng chữ "Sign up"
        String fullText = "Don’t have an account? Sign up";
        SpannableString spannable = new SpannableString(fullText);
        int start = fullText.indexOf("Sign up");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Navigation.findNavController(view).navigate(R.id.action_login_to_register);
            }
        };

        spannable.setSpan(clickableSpan, start, fullText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.txtSignUp.setText(spannable);
        binding.txtSignUp.setMovementMethod(LinkMovementMethod.getInstance());
        binding.txtSignUp.setHighlightColor(Color.TRANSPARENT);



    }



}
