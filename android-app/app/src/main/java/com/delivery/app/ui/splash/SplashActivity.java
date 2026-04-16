package com.delivery.app.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.delivery.app.DeliveryApplication;
import com.delivery.app.R;
import com.delivery.app.ui.auth.LoginActivity;
import com.delivery.app.ui.controller.DashboardControllerActivity;
import com.delivery.app.ui.livreur.DashboardLivreurActivity;
import com.delivery.app.util.Constants;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private ImageView logoImageView;
    private TextView appNameTextView;
    private TextView taglineTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initViews();
        animateLogo();
        checkAuthAndNavigate();
    }

    private void initViews() {
        logoImageView = findViewById(R.id.logoImageView);
        appNameTextView = findViewById(R.id.appNameTextView);
        taglineTextView = findViewById(R.id.taglineTextView);
    }

    private void animateLogo() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        logoImageView.startAnimation(fadeIn);
        appNameTextView.startAnimation(slideUp);
        taglineTextView.startAnimation(slideUp);
    }

    private void checkAuthAndNavigate() {
        new Handler().getLooper().prepareLooper();
        new Handler().postDelayed(() -> {
            DeliveryApplication app = DeliveryApplication.getInstance();
            String token = app.getAuthToken();

            if (token != null) {
                String role = app.getUserRole();
                if (Constants.ROLE_CONTROLLER.equals(role)) {
                    startActivity(new Intent(SplashActivity.this, DashboardControllerActivity.class));
                } else if (Constants.ROLE_LIVREUR.equals(role)) {
                    startActivity(new Intent(SplashActivity.this, DashboardLivreurActivity.class));
                } else {
                    navigateToLogin();
                }
            } else {
                navigateToLogin();
            }
            finish();
        }, 2000);
    }

    private void navigateToLogin() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }
}