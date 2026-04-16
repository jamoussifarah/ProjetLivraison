package com.delivery.app.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T> {
    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;

    @Nullable
    public final String token;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message, @Nullable String token) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.token = token;
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<>(Status.SUCCESS, data, null, null);
    }

    public static <T> Resource<T> success(@Nullable T data, @Nullable String token) {
        return new Resource<>(Status.SUCCESS, data, null, token);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msg, null);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null, null);
    }

    public enum Status {
        SUCCESS,
        ERROR,
        LOADING
    }
}