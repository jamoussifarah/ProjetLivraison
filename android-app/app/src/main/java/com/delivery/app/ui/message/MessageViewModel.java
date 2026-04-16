package com.delivery.app.ui.message;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.delivery.app.data.remote.model.Message;
import com.delivery.app.repository.LivraisonRepository;
import com.delivery.app.util.Resource;

import java.util.List;

public class MessageViewModel extends AndroidViewModel {
    private final LivraisonRepository repository;
    private final MutableLiveData<String> userFilter = new MutableLiveData<>();
    private LiveData<Resource<List<Message>>> messagesLiveData;

    public MessageViewModel(@NonNull Application application) {
        super(application);
        repository = new LivraisonRepository(application);
    }

    public LiveData<Resource<List<Message>>> getMessages(String toUser) {
        userFilter.setValue(toUser);
        if (messagesLiveData == null) {
            messagesLiveData = Transformations.switchMap(userFilter, user -> {
                return repository.getMessages(user);
            });
        }
        return messagesLiveData;
    }

    public LiveData<Resource<Message>> sendMessage(Message message) {
        return repository.sendMessage(message);
    }
}