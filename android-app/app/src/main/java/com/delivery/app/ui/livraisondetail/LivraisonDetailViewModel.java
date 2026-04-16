package com.delivery.app.ui.livraisondetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.delivery.app.data.remote.model.Livraison;
import com.delivery.app.data.remote.model.Message;
import com.delivery.app.repository.LivraisonRepository;
import com.delivery.app.util.Resource;

import java.util.List;

public class LivraisonDetailViewModel extends AndroidViewModel {
    private final LivraisonRepository repository;
    private final MutableLiveData<String> livraisonId = new MutableLiveData<>();
    private LiveData<Resource<Livraison>> livraisonLiveData;

    public LivraisonDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new LivraisonRepository(application);
    }

    public LiveData<Resource<Livraison>> getLivraison(String id) {
        livraisonId.setValue(id);
        if (livraisonLiveData == null) {
            livraisonLiveData = Transformations.switchMap(livraisonId, id -> {
                return repository.getTodayDeliveries();
            });
        }
        return livraisonLiveData;
    }

    public LiveData<Resource<Livraison>> updateLivraison(String id, String etat, String remarque) {
        MutableLiveData<Resource<Livraison>> result = new MutableLiveData<>();
        
        Livraison livraison = new Livraison();
        livraison.setId(id);
        livraison.setEtat(etat);
        livraison.setRemarque(remarque);
        
        repository.updateLivraison(id, livraison).observeForever(resource -> {
            result.setValue(resource);
        });
        
        return result;
    }

    public LiveData<Resource<Message>> sendMessage(Message message) {
        return repository.sendMessage(message);
    }
}