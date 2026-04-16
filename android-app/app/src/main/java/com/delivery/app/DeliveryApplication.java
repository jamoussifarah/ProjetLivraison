package com.delivery.app;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class DeliveryApplication extends Application {
    private static DeliveryApplication instance;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public static DeliveryApplication getInstance() {
        return instance;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public String getAuthToken() {
        return sharedPreferences.getString("auth_token", null);
    }

    public void setAuthToken(String token) {
        sharedPreferences.edit().putString("auth_token", token).apply();
    }

    public void clearAuthToken() {
        sharedPreferences.edit().remove("auth_token").apply();
    }

    public String getUserId() {
        return sharedPreferences.getString("user_id", null);
    }

    public void setUserId(String userId) {
        sharedPreferences.edit().putString("user_id", userId).apply();
    }

    public String getUserRole() {
        return sharedPreferences.getString("user_role", null);
    }

    public void setUserRole(String role) {
        sharedPreferences.edit().putString("user_role", role).apply();
    }

    public String getUserName() {
        return sharedPreferences.getString("user_name", null);
    }

    public void setUserName(String name) {
        sharedPreferences.edit().putString("user_name", name).apply();
    }
}