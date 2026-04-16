package com.delivery.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.delivery.app.DeliveryApplication;
import com.delivery.app.R;
import com.delivery.app.databinding.ActivityLoginBinding;
import com.delivery.app.ui.controller.DashboardControllerActivity;
import com.delivery.app.ui.livreur.DashboardLivreurActivity;
import com.delivery.app.util.Constants;
import com.delivery.app.util.Resource;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        animateViews();
        setupListeners();
        observeLogin();
    }

    private void animateViews() {
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        binding.logoContainer.startAnimation(slideUp);
        binding.loginCard.startAnimation(slideUp);
    }

    private void setupListeners() {
        binding.loginButton.setOnClickListener(v -> {
            String login = binding.loginInput.getText().toString().trim();
            String password = binding.passwordInput.getText().toString().trim();

            if (validateInputs(login, password)) {
                viewModel.login(login, password);
            }
        });
    }

    private boolean validateInputs(String login, String password) {
        boolean valid = true;

        if (login.isEmpty()) {
            binding.loginInputLayout.setError("Login requis");
            valid = false;
        } else {
            binding.loginInputLayout.setError(null);
        }

        if (password.isEmpty()) {
            binding.passwordInputLayout.setError("Mot de passe requis");
            valid = false;
        } else {
            binding.passwordInputLayout.setError(null);
        }

        return valid;
    }

    private void observeLogin() {
        viewModel.getLoginResult().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    showLoading(true);
                    break;
                case SUCCESS:
                    showLoading(false);
                    if (resource.data != null) {
                        saveUserData(resource.data, resource.token);
                        navigateToDashboard(resource.data.getRole());
                    }
                    break;
                case ERROR:
                    showLoading(false);
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void showLoading(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.loginButton.setEnabled(!show);
    }

    private void saveUserData(com.delivery.app.data.remote.model.User user, String token) {
        DeliveryApplication app = DeliveryApplication.getInstance();
        app.setAuthToken(token);
        app.setUserId(user.getId());
        app.setUserRole(user.getRole());
        app.setUserName(user.getFullName());
    }

    private void navigateToDashboard(String role) {
        Intent intent;
        if (Constants.ROLE_CONTROLLER.equals(role)) {
            intent = new Intent(this, DashboardControllerActivity.class);
        } else if (Constants.ROLE_LIVREUR.equals(role)) {
            intent = new Intent(this, DashboardLivreurActivity.class);
        } else {
            Toast.makeText(this, "Rôle non reconnu", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
        finish();
    }
}