package com.delivery.app.ui.livreur;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.delivery.app.DeliveryApplication;
import com.delivery.app.data.remote.model.Livraison;
import com.delivery.app.repository.LivraisonRepository;
import com.delivery.app.util.Resource;

import java.util.ArrayList;
import java.util.List;

public class DashboardLivreurViewModel extends AndroidViewModel {
    private final LivraisonRepository repository;
    private final String currentUserId;
    private final MutableLiveData<String> filterEtat = new MutableLiveData<>();
    private LiveData<Resource<List<Livraison>>> filteredLivraisons;

    public DashboardLivreurViewModel(@NonNull Application application) {
        super(application);
        repository = new LivraisonRepository(application);
        currentUserId = DeliveryApplication.getInstance().getUserId();
    }

    public LiveData<Resource<List<Livraison>>> getTodayDeliveries() {
        return repository.getTodayDeliveries();
    }

    public LiveData<Resource<List<Livraison>>> filterByStatus(String etat) {
        filterEtat.setValue(etat);
        if (filteredLivraisons == null) {
            filteredLivraisons = Transformations.switchMap(filterEtat, status -> {
                if (status == null) {
                    return repository.getTodayDeliveries();
                } else {
                    return repository.getDeliveries(null, currentUserId, status, null);
                }
            });
        }
        return filteredLivraisons;
    }

    public void syncPendingDeliveries() {
        List<Livraison> pending = new ArrayList<>();
        repository.syncLivraisons(pending);
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public String getUserName() {
        return DeliveryApplication.getInstance().getUserName();
    }
}