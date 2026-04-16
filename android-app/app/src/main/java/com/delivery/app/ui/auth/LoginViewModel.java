package com.delivery.app.ui.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.delivery.app.data.remote.model.User;
import com.delivery.app.repository.LivraisonRepository;
import com.delivery.app.util.Resource;

public class LoginViewModel extends AndroidViewModel {
    private final LivraisonRepository repository;
    private final MutableLiveData<Resource<User>> loginResult = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new LivraisonRepository(application);
    }

    public LiveData<Resource<User>> getLoginResult() {
        return loginResult;
    }

    public void login(String login, String password) {
        LiveData<Resource<User>> result = repository.login(login, password);
        result.observeForever(resource -> {
            if (resource != null) {
                loginResult.setValue(resource);
            }
        });
    }
}